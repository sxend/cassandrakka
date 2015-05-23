package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.pattern._
import akka.util.Timeout
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols.GetConfig

import scala.collection.mutable
import scala.concurrent.Future

class NodeActor(components: {
  val defaultTimeout: Timeout
  val configurationActor: ActorModule[ConfigurationActor]
  val connectionActor: (InetSocketAddress, Int, ActorModule[NodeActor]) => ActorModule[ConnectionActor]
}, module: ActorModule[NodeActor], remote: InetSocketAddress) extends Actor with ActorLogging {
  implicit val timeout = components.defaultTimeout
  import context.dispatcher

  val configurationActor = components.configurationActor

  import arimitsu.sf.cassandrakka.actors.NodeActor.Protocols._

  private val connections = mutable.Map[String, ActorModule[ConnectionActor]]()

  def receive = {
    case ConnectAll => connectAll()
    case Connect(number) => connect(number)
    case AddConnection(id, connection) =>
      connections.put(id, connection)
      Future(connection).pipeTo(sender())
    case message@Stopped(stoppedRemote, number) if remote.getHostString == stoppedRemote.getHostString =>
      log.warning(s"Connection Actor is Stopped. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      connect(number)
    case message => log.warning(s"Unhandled Message. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  def connectAll() = {
    configurationActor.typedAsk(GetConfig).map {
      config =>
        (1 until config.getInt("connection-per-node")).foreach {
          number => connect(number)
        }
    }
  }

  private def connect(number: Int) = {
    val connection = components.connectionActor(remote, number, module)
    val session = components.sessionActor(connection)
    context.self ! AddConnection(remote.getHostString + number, connection)
  }
}

object NodeActor {

  object Protocols {

    case object ConnectAll

    case class Connect(number: Int)

    case class AddConnection(id: String, connection: ActorModule[ConnectionActor])

    case class Stopped(stoppedRemote: InetSocketAddress, number: Int)

  }

}