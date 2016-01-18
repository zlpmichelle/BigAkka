package org.akka.example.wordcount.server;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReduceActor extends UntypedActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef actor = null;

	public ReduceActor(ActorRef inAggregateActor) {
		actor = inAggregateActor;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof List) {

			@SuppressWarnings("unchecked")
			List<Result> work = (List<Result>) message;

			// perform the work
			NavigableMap<String, Integer> reducedList = reduce(work);

			// reply with the result
			actor.tell(reducedList, self());
      // log.info("***********I'm in Reduce Actor on Receive, message: "
      // + message.toString() + "*******\n");
      // actor.tell("*********I'm in Reduicer Actor on Receive \n");

      Iterator i = reducedList.entrySet().iterator();
      while (i.hasNext()) {
        System.out.println("++++++ reduce done " + i.next());
      }

		} else
			throw new IllegalArgumentException("Unknown message [" + message
					+ "]");
	}

	private NavigableMap<String, Integer> reduce(List<Result> list) {

		NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<String, Integer>();

		Iterator<Result> iter = list.iterator();
		while (iter.hasNext()) {
			Result result = iter.next();
			if (reducedMap.containsKey(result.getWord())) {
				Integer value = (Integer) reducedMap.get(result.getWord());
				value++;
				reducedMap.put(result.getWord(), value);
			} else {
				reducedMap.put(result.getWord(), Integer.valueOf(1));
			}
		}
		return reducedMap;
	}
}
