package arimitsu.sf.cassandrakka

import akka.actor.ActorSystem
import arimitsu.sf.cassandrakka.actors.SessionActor
import arimitsu.sf.cassandrakka.actors.SessionActor.Protocols.Options

import scala.concurrent.{Future, ExecutionContext}

abstract class Session(components: {
  val system: ActorSystem
}, sessionActor: ActorModule[SessionActor]){
  import components.system.dispatcher

  private[cassandrakka] def options() = {
    sessionActor.typedAsk(Options())
  }

  //  def execute(id: Byte, arg: Any*): Future[AnyRef]
}
