package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.NodeActor.Protocols._

class ConnectionActor(components: {
}, module: ActorModule[ConnectionActor], remote: InetSocketAddress, number: Int, nodeManagerModule: ActorModule[NodeActor]) extends Actor with ActorLogging {

  import arimitsu.sf.cassandrakka.actors.ConnectionActor.Protocols._
  import context.system

  private var connection: ActorRef = _


  def receive = {
    case message@CommandFailed(c: Tcp.Connect) =>
      log.warning(s"Connect command failed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      context stop self
    case c@Connected(connectedRemote, local) =>
      connection = sender()
      connection ! Register(self)
    case message@Closed =>
      log.warning(s"Connection closed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      nodeManagerModule.actorRef ! Stopped(remote, number)
      context stop self
    case ReConnect =>
      connect()
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  private def connect() = {
    IO(Tcp) ! Tcp.Connect(remoteAddress = remote)
  }

  connect()
}

object ConnectionActor {

  object Protocols {

    case object ReConnect

  }

}

