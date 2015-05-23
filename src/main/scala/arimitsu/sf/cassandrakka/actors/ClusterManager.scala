package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ClusterManager.Protocol._
import arimitsu.sf.cassandrakka.actors.ConfigurationManager.Protocols._

import scala.collection.mutable
import scala.collection.JavaConversions._

class ClusterManager(components: {
  val configurationManager: ActorModule[ConfigurationManager]
  val nodeManager: InetSocketAddress => ActorModule[NodeManager]
}) extends Actor with ActorLogging {
  import context.dispatcher
  private val configurationManager = components.configurationManager
  private val nodes = mutable.Map[String, ActorModule[NodeManager]]()

  def receive = {
    case AddNode(node) => nodes.put(node.toString, node)
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  def start() {
    configurationManager.typedAsk(GetConfig).map {
      config =>
        config.getConfigList("nodes").foreach{
          nodeConfig =>
            val host = nodeConfig.getString("address")
            val port = nodeConfig.getInt("port")
            val remote = new InetSocketAddress(host, port)
            val node = components.nodeManager(remote)
            self ! AddNode(node)
        }
    }
  }

  start()
}

object ClusterManager {

  object Protocol {

    case object GetSession

    case class AddNode(remote: ActorModule[NodeManager])

  }

}