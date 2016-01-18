package hadoopwc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WordCountReduceActor extends UntypedActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private ActorRef actor = null;

  public WordCountReduceActor(ActorRef inAggregateActor) {
    actor = inAggregateActor;
  }

  private IntWritable result = new IntWritable();

  public List<MapReduceInfoCollect> reduce(List<MiddleResult> list) throws IOException, InterruptedException {
    Iterator<MiddleResult> iter = list.iterator();
    List<MapReduceInfoCollect> info = new ArrayList<MapReduceInfoCollect>();
   while(iter.hasNext()){
      System.out.println("======= enter : ");
     MiddleResult midR = iter.next();
     Iterator<MapReduceInfoCollect> infoIter = info.iterator();
     boolean find = false;
     while(infoIter.hasNext()){
       MapReduceInfoCollect  mrinfo = infoIter.next();
        if (mrinfo.getFilePath().contains(midR.getWord())) {
          System.out.println("======= true : ");
         find = true;
         int sum = mrinfo.getRecordNum();
         sum++;
          // info.add(new MapReduceInfoCollect(midR.getWord(), sum));
          // mrinfo.setFilePath(midR.getWord());
          mrinfo.setRecordNum(sum);
        } 
     }

      if (!find) {
        System.out.println("======= false : ");
        info.add(new MapReduceInfoCollect(midR.getWord(), 1));
    }
    }
    return info;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReceive(Object message) throws Exception {
    System.out.println("======= reduce message is : " + message);
    if (message instanceof List) {
    @SuppressWarnings("unchecked")
    List<MiddleResult> work = (List<MiddleResult>) message;

    // perform the work
     List<MapReduceInfoCollect> info = reduce(work);

    // reply with the result
      Iterator<MapReduceInfoCollect> infoiter = info.iterator();
      while (infoiter.hasNext()) {
        MapReduceInfoCollect mri = infoiter.next();
        actor.tell(mri, self());
        System.out.println("++++++ reduce done " + mri);
      }
    } else
      throw new IllegalArgumentException("Unknown message [" + message + "]");
  }
}
