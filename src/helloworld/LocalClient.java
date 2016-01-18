package helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class LocalClient implements Bootable {

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("LocalClientApp", ConfigFactory
        .load().getConfig("LocalClientApp"));

    final ActorRef remoteActor = system
        .actorFor("akka://RemoteServerApp@127.0.0.1:2552/user/remoteServerActor");

    @SuppressWarnings("serial")
    ActorRef actor = system.actorOf(new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new LocalClientActor(remoteActor);
      }
    }));
    remoteActor.tell("Display", null);
    actor.tell("zhangliping", null);
    system.shutdown();
  }


  public void shutdown() {
  }


  public void startup() {
  }

}
