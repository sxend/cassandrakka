package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Props, ActorRef, Actor}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ConfigurationManager.Protocols.GetConfig

import scala.collection.mutable

class NodeManager(components: {
  val configurationManager: ActorModule[ConfigurationManager]
  val connectionManager: InetSocketAddress => ActorModule[ConnectionManager]
}, remote: InetSocketAddress) extends Actor {
  val configurationManager = components.configurationManager

  import arimitsu.sf.cassandrakka.actors.NodeManager.Protocol._

  private val connections = mutable.Seq[ActorModule[ConnectionManager]]()

  def receive = {
    case Connect => connect()
    case AddConnection(connection) => connections :+ connection
    case _ =>
  }

  def connect() = {
    configurationManager.typedAsk(GetConfig).map {
      config =>
        (0 until config.getInt("connection-per-node")).foreach {
          _ =>
            val connection = components.connectionManager(remote)
            context.self ! AddConnection(connection)
        }
    }
  }
}

object NodeManager {

  object Protocol {

    case object Connect

    case class AddConnection(connection: ActorModule[ConnectionManager])

  }

}