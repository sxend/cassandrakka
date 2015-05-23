package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.ActorModule.Mapping
import arimitsu.sf.cassandrakka.actors.NodeActor.Protocols._

class SessionActor(components: {
}, module: ActorModule[SessionActor], remote: InetSocketAddress, number: Int, nodeManagerModule: ActorModule[NodeActor]) extends Actor with ActorLogging {

  import arimitsu.sf.cassandrakka.actors.SessionActor.Protocols._
  import context.system
  private var stream: Short = 0
  private var isStartup: Boolean = false
  private var connection: ActorRef = _

  def receive = {
    case message@CommandFailed(c: Tcp.Connect) =>
      log.warning(s"Connect command failed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      context stop self
    case c@Connected(connectedRemote, local) =>
      connection = sender()
      connection ! Tcp.Register(self)
    case message@Closed =>
      log.warning(s"Connection closed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      nodeManagerModule.actorRef ! Stopped(remote, number)
      context stop self
    case ReConnect =>
      connect()
    case req@Startup =>

    case req@AuthResponse =>
    case req@Options =>
    case req@Query =>
    case req@Prepare =>
    case req@Execute =>
    case req@Batch =>
    case req@Register =>

    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  private def connect() = {
    IO(Tcp) ! Tcp.Connect(remoteAddress = remote)
  }

  connect()
}

object SessionActor {

  object Protocols {

    case object ReConnect

    case class Send(data: ByteString)

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

