package modifywordcount;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RemoteServerActor extends UntypedActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  @SuppressWarnings("deprecation")
  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      if (((String) message).compareTo("Display") == 0) {
        this.getSender().tell(message);
        System.out.println("===Got Display Message : " + message);
      } else {
        this.getSender().tell(message);
        System.out.println("==Other Display Message From Client Actor : "
            + message);
      }
      // log.info("Server on receive actor");
    }
  }
}
