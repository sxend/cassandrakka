package arimitsu.sf.cassandrakka

import akka.actor._
import arimitsu.sf.cassandrakka.actors.{ConfigurationManager, ClusterManager}

import scala.reflect.ClassTag

trait Components {
  self =>
  import Components._
  implicit val components = self
  implicit val system: ActorSystem
  val configurationManager: ActorModule[ConfigurationManager] = ActorModule[ConfigurationManager]()
  val clusterManager: ActorModule[ClusterManager] = ActorModule[ClusterManager]()
}

object Components {
  case class ActorModule[A <: Actor](implicit tag: ClassTag[A], system: ActorRefFactory, components: Components){
    def toActorRef: ActorRef = system.actorOf(Props(tag.runtimeClass, components))
  }
}