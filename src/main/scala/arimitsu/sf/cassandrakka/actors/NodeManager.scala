package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Props, ActorRef, Actor}

import scala.collection.mutable

class NodeManager(remote: InetSocketAddress) extends Actor {
  import arimitsu.sf.cassandrakka.actors.NodeManager.Protocol._
  private val connections = new mutable.HashSet[ActorRef]()
  def receive = {
    case Connect =>
      val connection = context.actorOf(Props(classOf[ConnectionManager], remote))
      connections.add(connection)
    case _ =>
  }
}

object NodeManager {
  object Protocol {
    case object Connect
  }
}