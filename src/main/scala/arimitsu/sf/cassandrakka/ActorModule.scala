package arimitsu.sf.cassandrakka

import akka.actor._
import akka.util._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class ActorModule[A <: Actor](moduleToProps: => (ActorModule[A]) => Props, name: => String = "")(implicit system: ActorSystem, val timeout: Timeout) {

  lazy val actorRef: ActorRef = name match {
    case "" => system.actorOf(moduleToProps(this))
    case _ => system.actorOf(moduleToProps(this), name)
  }
}

object ActorModule {

  trait Mapping[M <: Actor, A, B] {
  }
  object Mapping{
    def apply[M <: Actor, A, B]: Mapping[M, A, B] = new Mapping[M, A, B] {}
  }

  implicit class Reply[M <: Actor, A, B, R <: Mapping[M, A, B]](a: A){

    def reply(b: => Future[B])(implicit mapping: Mapping[M, A, B]) : TypedPipeTo[M, A, B] = b
  }

  implicit class TypedPipeTo[M <: Actor, A, B](b: Future[B]) {

    import akka.pattern.pipe

    def typedPipeTo(sender: ActorRef)(implicit ec: ExecutionContext, mapping: Mapping[M, A, B]): Future[B] = b.pipeTo(sender)
  }

  implicit class TypedAsk[M <: Actor](module: ActorModule[M]) {

    import module.timeout

    import akka.pattern.ask

    def typedAsk[A, B](message: => A)(implicit mapping: Mapping[M, A, B], ec: ExecutionContext, tag: ClassTag[B]): Future[B] = module.actorRef.ask(message).mapTo[B]
  }

}
