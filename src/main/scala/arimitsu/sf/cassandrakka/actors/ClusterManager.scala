package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Props, ActorRef, Actor}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ClusterManager.Protocol._
import arimitsu.sf.cassandrakka.actors.ConfigurationManager.Protocols._

import scala.collection.mutable

class ClusterManager(components: {
  val configurationManager: ActorModule[ConfigurationManager],
  val nodeManager: InetSocketAddress => ActorModule[NodeManager]
}) extends Actor {
  private val configurationManager = components.configurationManager
  private val nodes = mutable.Map[String, ActorModule[NodeManager]]()
  def receive = {
    case AddNode(node) => nodes.put(node.toString, node)
    case _ =>
  }
  def start() {
    configurationManager.typedAsk(GetConfig).map{
      config =>
        config.get
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