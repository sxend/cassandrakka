package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import akka.pattern._
import arimitsu.sf.cassandrakka.ActorModule.Mapping
import arimitsu.sf.cassandrakka.{Session, ActorModule}
import arimitsu.sf.cassandrakka.actors.ClusterActor.Protocols._
import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols._

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.concurrent.Future

class ClusterActor(components: {
  val defaultTimeout: Timeout
  val configurationManager: ActorModule[ConfigurationActor]
  val nodeManager: InetSocketAddress => ActorModule[NodeActor]
}, module: ActorModule[ClusterActor]) extends Actor with ActorLogging {
  implicit val timeout = components.defaultTimeout

  import context.dispatcher

  private val configurationManager = components.configurationManager
  private val nodes = mutable.Map[String, ActorModule[NodeActor]]()
  private var ring = Iterator.continually(nodes).flatten

  def receive = {
    case AddNode(node) =>
      nodes.put(node.toString, node)
      ring = Iterator.continually(nodes).flatten
    case GetSession => getSession.mapTo[ActorModule[SessionActor]].pipeTo(sender())
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  def getSession: Future[ActorModule[SessionActor]] = {
    ring.next()._2.typedAsk(NodeActor.Protocols.GetSession)
  }

  def start() {
    configurationManager.typedAsk(GetConfig()).map {
      config =>
        config.getConfigList("nodes").foreach {
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

object ClusterActor {

  object Protocols {

    case object GetSession

    case class AddNode(remote: ActorModule[NodeActor])

    implicit object GetSessionMapping extends Mapping[ClusterActor, GetSession.type , ActorModule[SessionActor]]
  }

}