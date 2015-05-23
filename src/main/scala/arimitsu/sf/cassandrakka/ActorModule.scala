package arimitsu.sf.cassandrakka

import akka.actor._
import akka.util._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class ActorModule[A <: Actor](props: => Props, name: => String = "")(implicit system: ActorSystem) {
  def toActorRef: ActorRef = name match {
    case "" => system.actorOf(props)
    case _ => system.actorOf(props, name)
  }
}

object ActorModule {

  trait Mapping[M <: Actor, A, B] {
  }

  implicit class TypedAsk[M <: Actor](module: ActorModule[M]) {

    import akka.pattern.ask

    def typedAsk[A, B](message: => A)(implicit mapping: Mapping[M, A, B], ec: ExecutionContext, timeout: Timeout, tag: ClassTag[B]): Future[B] = module.toActorRef.ask(message).mapTo[B]
  }

}
