package arimitsu.sf.cassandrakka.actors

import akka.actor.{Actor, ActorLogging}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.ActorModule.Mapping

class SessionActor(components: {
  val connectionActor: ActorModule[ConnectionActor]
}, module: ActorModule[SessionActor]) extends Actor with ActorLogging {
  lazy val connection = components.connectionActor

  import arimitsu.sf.cassandrakka.actors.SessionActor._
  import Protocols._

  var stream: Short = 0
  var isStartup: Boolean = false
  var sessionContext = SessionContext()

  def receive = {
    case req@Startup =>
      if (!isStartup) {
      }
    case req@AuthResponse =>
    case req@Options =>
    case req@Query =>
    case req@Prepare =>
    case req@Execute =>
    case req@Batch =>
    case req@Register =>

    case message =>
      log.warning(s"Unhandled Message. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  private def prepareOp(req: Operation) = {
    if (!isStartup) {
      log.warning(s"this session is not Startup yet. operation: $req, sender: ${sender().toString()}, self: ${self.toString()}")
      self ! req
    }
  }

  private def streamCountUp: Short = {
    stream = (stream + 1).toShort
    stream
  }
}

object SessionActor {

  case class SessionContext(isStartuped: Boolean = false, streamId: Int = 0)

  object Protocols {

    sealed trait Operation

    sealed trait Requests extends Operation

    sealed trait Responses extends Operation

    sealed trait Events extends Operation

    case class Startup() extends Requests

    case class AuthResponse() extends Requests

    case class Options() extends Requests

    case class Query() extends Requests

    case class Prepare() extends Requests

    case class Execute() extends Requests

    case class Batch() extends Requests

    case class Register() extends Requests

    case class Error() extends Responses

    case class Ready() extends Responses

    case class Authenticate() extends Responses

    case class Supported() extends Responses

    case class Result() extends Responses

    case class AuthChallenge() extends Responses

    case class AuthSuccess() extends Responses

    case class TopologyChange() extends Events

    case class StatusChange() extends Events

    case class SchemaChange() extends Events

    implicit object StartupMapping extends Mapping[SessionActor, Startup, Either[Throwable, Either[Authenticate, Ready]]]

    implicit object AuthResponseMapping extends Mapping[SessionActor, AuthResponse, Either[Throwable, Either[AuthChallenge, AuthSuccess]]]

    implicit object OptionsMapping extends Mapping[SessionActor, Options, Either[Throwable, Supported]]

    implicit object QueryMapping extends Mapping[SessionActor, Query, Either[Throwable, Result]]

    implicit object PrepareMapping extends Mapping[SessionActor, Prepare, Either[Throwable, Result]]

    implicit object ExecuteMapping extends Mapping[SessionActor, Execute, Either[Throwable, Result]]

    implicit object BatchMapping extends Mapping[SessionActor, Batch, Either[Throwable, Result]]

    implicit object RegisterMapping extends Mapping[SessionActor, Register, Either[Throwable, Ready]]

  }

}