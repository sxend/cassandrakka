package arimitsu.sf.cassandrakka

import java.net.InetSocketAddress

import akka.actor._
import akka.util.Timeout
import arimitsu.sf.cassandrakka.actors._

import scala.concurrent.ExecutionContext

trait Components {
  self =>
  import scala.concurrent.duration._
  implicit val components = self
  implicit val system: ActorSystem
  val systemEC: ExecutionContext = system.dispatcher
  val defaultTimeout: Timeout = 1 second
  val configurationManager: ActorModule[ConfigurationActor] =
    new ActorModule[ConfigurationActor](module => Props(classOf[ConfigurationActor], components, module))
  val clusterActor: ActorModule[ClusterActor] =
    new ActorModule[ClusterActor](module => Props(classOf[ClusterActor], components, module))
  val connectionManager =
    (remote: InetSocketAddress, number: Int, nodeManagerModule: ActorModule[NodeActor]) => new ActorModule[ConnectionActor](module => Props(classOf[ConnectionActor], components, module, remote, nodeManagerModule, sessionActorModule))
  val nodeManager =
    (remote: InetSocketAddress) => new ActorModule[NodeActor](module => Props(classOf[NodeActor], components, module, remote))
  val sessionActor = new ActorModule[SessionActor](module => Props(classOf[NodeActor], components))
}
