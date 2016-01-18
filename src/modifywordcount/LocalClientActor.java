package modifywordcount;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LocalClientActor extends UntypedActor {

  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private ActorRef remoteActor;

  public LocalClientActor(ActorRef inRemoteServer) {
    remoteActor = inRemoteServer;
    remoteActor.tell("hello world", self());
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      String msg = (String) message;
      remoteActor.tell("client on Receive message : " + msg, self());
      remoteActor.tell("I'm in LocalClientActor onReceive", self());
      // log.info("Client on receive actor");
      testFunction();
    }
  }

  public void testFunction() {
    remoteActor.tell("test from other test function", self());
  }
}
