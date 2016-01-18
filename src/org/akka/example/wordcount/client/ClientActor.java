package org.akka.example.wordcount.client;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClientActor extends UntypedActor {

  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef remoteServer = null;
	@SuppressWarnings("unused")
	private ActorRef fileReadActor = null;
	private long start;

	/**
	 * @param args
	 */
	public ClientActor(ActorRef inRemoteServer) {

		remoteServer = inRemoteServer;
    // remoteServer.tell("*********I'm in ClientActor new instance");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
      remoteServer.tell(msg, self());
      remoteServer.tell("*********I'm in ClientActor onReceive", self());
      log.info("client on receive  " + remoteServer.toString());
		}
	}

	@Override
	public void preStart() {
		start = System.currentTimeMillis();
	}

	@Override
	public void postStop() {
		// tell the world that the calculation is complete
		long timeSpent = (System.currentTimeMillis() - start) / 1000;
		System.out
				.println(String
						.format("\n\tClientActor estimate: \t\t\n\tCalculation time: \t%s Secs",
								timeSpent));
	}
}
