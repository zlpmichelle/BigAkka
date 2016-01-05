package modifywordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WordCount extends UntypedActor {

  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private static ActorRef remoteActor;

  public WordCount(ActorRef inRemoteServer) {
    remoteActor = inRemoteServer;
    remoteActor
        .tell("Hello World, testing Word Count Mapreduce by using akka to collect log");
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      String msg = (String) message;
      remoteActor.tell("client on Receive message : " + msg);
      remoteActor.tell("I'm in WordCount onReceive");
      // log.info("Client on receive actor");
      String[] paths = new String[] { "hdfs://localhost:9100/user/root/input",
          "hdfs://localhost:9100/user/root/output" };
      testFunction(paths);
    }
  }

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
        remoteActor.tell("in Mapper, word is " + word.toString());
      }
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
      remoteActor.tell("in Reducer, key is " + key.toString() + ", number is "
          + sum);
    }
  }

  public void testFunction(String[] args) throws Exception {
    remoteActor.tell("begin running wordcount mapreduce");
    Configuration conf = new Configuration();
    // conf.set("fs.default.name", "file:///");
    // conf.set("mapred.job.tracker", "local");

    if (args.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.waitForCompletion(true);
    remoteActor.tell("finished running wordcount mapreduce");
  }
}
