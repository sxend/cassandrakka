package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.NodeManager.Protocol._

class ConnectionManager(components: {
}, remote: InetSocketAddress, number: Int, nodeManager: ActorModule[NodeManager]) extends Actor with ActorLogging {

  import arimitsu.sf.cassandrakka.actors.ConnectionManager.Protocol._
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
      nodeManager.toActorRef ! Stopped(remote, number)
      context stop self
    case ReConnect =>
      connect()
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  private def connect() = {
    IO(Tcp) ! Connect(remoteAddress = remote)
  }

  connect()
}

object ConnectionManager {

  object Protocol {

    case object ReConnect

  }

}

