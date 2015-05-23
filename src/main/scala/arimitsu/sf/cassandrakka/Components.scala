package arimitsu.sf.cassandrakka

import java.net.InetSocketAddress

import akka.actor._
import arimitsu.sf.cassandrakka.actors.{NodeManager, ConnectionManager, ConfigurationManager, ClusterManager}

trait Components {
  self =>
  implicit val components = self
  implicit val system: ActorSystem
  val configurationManager: ActorModule[ConfigurationManager] =
    new ActorModule[ConfigurationManager](Props(classOf[ConfigurationManager], components))
  val clusterManager: ActorModule[ClusterManager] =
    new ActorModule[ClusterManager](Props(classOf[ClusterManager], components))
  val connectionManager =
    (remote: InetSocketAddress, number: Int) => new ActorModule[ConnectionManager](Props(classOf[ConnectionManager], components, remote))
  val nodeManager =
    (remote: InetSocketAddress) => new ActorModule[NodeManager](Props(classOf[NodeManager], components, remote))
}
