package arimitsu.sf.cassandrakka

import akka.actor._
import akka.util._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class ActorModule[A <: Actor](moduleToProps: => (this.type) => Props, name: => String = "")(implicit system: ActorSystem) {

  lazy val actorRef: ActorRef = name match {
    case "" => system.actorOf(moduleToProps(this))
    case _ => system.actorOf(moduleToProps(this), name)
  }
}

object ActorModule {

  trait Mapping[M <: Actor, A, B] {
  }

  implicit class TypedAsk[M <: Actor](module: ActorModule[M]) {

    import akka.pattern.ask

    def typedAsk[A, B](message: => A)(implicit mapping: Mapping[M, A, B], ec: ExecutionContext, timeout: Timeout, tag: ClassTag[B]): Future[B] = module.actorRef.ask(message).mapTo[B]
  }

}
