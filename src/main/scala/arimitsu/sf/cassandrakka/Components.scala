package arimitsu.sf.cassandrakka

import java.net.InetSocketAddress

import akka.actor._
import arimitsu.sf.cassandrakka.actors.{ClusterManager, ConfigurationManager, ConnectionManager, NodeManager}

trait Components {
  self =>
  implicit val components = self
  implicit val system: ActorSystem
  val configurationManager: ActorModule[ConfigurationManager] =
    new ActorModule[ConfigurationManager](module => Props(classOf[ConfigurationManager], components, module))
  val clusterManager: ActorModule[ClusterManager] =
    new ActorModule[ClusterManager](module => Props(classOf[ClusterManager], components, module))
  val connectionManager =
    (remote: InetSocketAddress, number: Int, nodeManagerModule: ActorModule[NodeManager]) => new ActorModule[ConnectionManager](module => Props(classOf[ConnectionManager], components, module, remote, nodeManagerModule))
  val nodeManager =
    (remote: InetSocketAddress) => new ActorModule[NodeManager](module => Props(classOf[NodeManager], components, module, remote))
}
