package helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class RemoteServer implements Bootable {
  
  private ActorSystem system;
  @SuppressWarnings("unused")
  private ActorRef remoteActor;

  @SuppressWarnings("serial")
  public RemoteServer() {
    system = ActorSystem.create("RemoteServerApp", ConfigFactory.load().getConfig("RemoteServerApp"));
    remoteActor = system.actorOf(new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new RemoteServerActor();
      }
    }), "remoteServerActor");
    
  }

  public static void main(String[] args) {
    new RemoteServer();
  }

  @Override
  public void shutdown() {
    // system.shutdown();
  }

  @Override
  public void startup() {
    // system.actorOf(new Props(RemoteServerActor.class), "remoteServerActor");
  }

}
