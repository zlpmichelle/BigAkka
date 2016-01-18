package hadoopwc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WordCountMapActor extends UntypedActor {
  private static ActorRef actor = null;
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  public WordCountMapActor(ActorRef inReduceActor) {
    actor = inReduceActor;
  }

  private static IntWritable one = new IntWritable(
      1);
  private String word = new String();

  public List<MiddleResult> map(String value) throws IOException,
      InterruptedException {
    List<MiddleResult> list = new ArrayList<MiddleResult>();
    StringTokenizer itr = new StringTokenizer(value.toString());
    while (itr.hasMoreTokens()) {
      word = itr.nextToken().toLowerCase();
      list.add(new MiddleResult(word, one));
    }
    return list;
  }

  @Override
  public void onReceive(Object message) throws Exception {
    System.out.println("=======message is : " + message);
    if (message instanceof String) {
      String work = (String) message;

      // perform the work
      List<MiddleResult> list = map(work);

      // reply with the result
      actor.tell(list, self());
      log.info("=====print log info on server port");
    } else
      throw new IllegalArgumentException("Unknown message [" + message + "]");
  }
}