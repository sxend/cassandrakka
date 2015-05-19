package arimitsu.sf.cassandrakka

import akka.actor._

import scala.reflect.ClassTag

trait Components {
  self =>
  implicit val components = self
}

object Components {
  case class ActorModule[A <: Actor](implicit tag: ClassTag[A], system: ActorRefFactory, components: Components){
    def toActorRef: ActorRef = system.actorOf(Props(tag.runtimeClass, components))
  }
}