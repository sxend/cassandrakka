package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import akka.pattern._
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ConfigurationManager.Protocols.GetConfig

import scala.collection.mutable
import scala.concurrent.Future

class NodeManager(components: {
  val configurationManager: ActorModule[ConfigurationManager]
  val connectionManager: (InetSocketAddress, Int) => ActorModule[ConnectionManager]
}, remote: InetSocketAddress) extends Actor with ActorLogging {
  import context.dispatcher
  val configurationManager = components.configurationManager

  import arimitsu.sf.cassandrakka.actors.NodeManager.Protocol._

  private val connections = mutable.Map[String, ActorModule[ConnectionManager]]()

  def receive = {
    case ConnectAll => connectAll()
    case Connect(number) => connect(number)
    case AddConnection(id ,connection) =>
      connections.put(id, connection)
      Future(connection).pipeTo(sender())
    case message@Stopped(stoppedRemote, number) if remote.getHostString == stoppedRemote.getHostString =>
      log.warning(s"Connection Manager is Stopped. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      connect(number)
    case message => log.warning(s"Unhandled Message. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  def connectAll() = {
    configurationManager.typedAsk(GetConfig).map {
      config =>
        (1 until config.getInt("connection-per-node")).foreach {
          number => connect(number)
        }
    }
  }
  private def connect(number: Int) = {
    val connection = components.connectionManager(remote, number)
    context.self ! AddConnection(remote.getHostString + number, connection)
  }
}

object NodeManager {

  object Protocol {

    case object ConnectAll

    case class Connect(number: Int)

    case class AddConnection(id: String, connection: ActorModule[ConnectionManager])
  
    case class Stopped(stoppedRemote: InetSocketAddress, number: Int)
  }

}