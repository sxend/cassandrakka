package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{ActorLogging, ActorRef, Actor}
import akka.io.Tcp.{CommandFailed, Register, Connected, Connect}
import akka.io.{Tcp, IO}

class ConnectionManager(remoteAddress: InetSocketAddress) extends Actor with ActorLogging {
  import context.system
  import arimitsu.sf.cassandrakka.actors.ConnectionManager.Protocol._

  private var connection: ActorRef = _


  def receive = {
    case CommandFailed(_: Connect) =>
      log.warning(s"$self send command failed")
      context stop self
    case c @ Connected(remote, local) =>
      connection = sender()
      connection ! Register(self)
    case ReConnect =>
      connect()
  }
  private def connect() = {
    IO(Tcp) ! Connect(remoteAddress = remoteAddress)
  }
  connect()
}
object ConnectionManager {
  object Protocol {
    case object ReConnect
  }
}

