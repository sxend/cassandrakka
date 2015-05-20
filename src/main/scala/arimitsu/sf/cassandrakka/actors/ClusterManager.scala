package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Props, ActorRef, Actor}
import arimitsu.sf.cassandrakka.actors.ClusterManager.Protocol.AddNode

import scala.collection.mutable

class ClusterManager(components: {
  val configurationManager: ActorRef
}) extends Actor {
  private val nodes = new mutable.HashSet[ActorRef]()
  def receive = {
    case AddNode(remote) =>
      val node = context.actorOf(Props(classOf[NodeManager], remote))
      nodes.add(node)
    case _ =>
  }
}

object ClusterManager {
  object Protocol {
    case object GetSession
    case class AddNode(remote: InetSocketAddress)
  }
}