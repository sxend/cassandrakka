package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.ActorModule.Mapping
import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols.GetCompression
import arimitsu.sf.cassandrakka.actors.NodeActor.Protocols._
import arimitsu.sf.cassandrakka.cql.Compressions.Raw
import arimitsu.sf.cassandrakka.cql._
import akka.pattern.ask
import akka.pattern.pipe
import Notations._

import scala.collection.mutable
import scala.concurrent.Promise

class SessionActor(components: {
  val configurationActor: ActorModule[ConfigurationActor]
  val cqlParser: () => CQLParser
}, module: ActorModule[SessionActor], remote: InetSocketAddress, number: Int, nodeManagerModule: ActorModule[NodeActor]) extends Actor with ActorLogging {
  import context.dispatcher
  import arimitsu.sf.cassandrakka.actors.SessionActor.Protocols._
  import context.system
  private val promises =  mutable.Map[Short, Promise[_]]()
  private var stream: Short = 0
  private var isStartup: Boolean = false
  private var connection: Option[ActorRef] = None
  private var compression: Compression = Raw

  components.configurationActor.typedAsk(GetCompression()).onSuccess{
    case compression =>self ! SetCompression(compression)
  }

  private def isConnected = connection.isDefined

  private def withConnection[A](message: A)(f: => ActorRef => Unit) = {
    if (isConnected) {
      f(connection.get)
    } else {
      self ! message
    }
  }

  def receive = {
    case message@CommandFailed(c: Tcp.Connect) =>
      log.warning(s"Connect command failed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      context stop self
    case c@Connected(connectedRemote, local) =>
      val conn = sender()
      connection = Option(conn)
      conn ! Tcp.Register(self)
    case message@Closed =>
      log.warning(s"Connection closed. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      nodeManagerModule.actorRef ! Stopped(remote, number)
      context stop self
    case ReConnect =>
      connection = None
      connection.get ! Tcp.Close
      connect()
    case SetCompression(comp) =>
      compression = comp
    case req: Startup =>
    case req: AuthResponse =>
    case req: Options =>
    case req: Query =>
    case req: Prepare =>
    case req: Execute =>
    case req: Batch =>
    case req: Register =>
    case send: Send[_] =>
    case Received(data) =>
      val frame = Frame.parse(data.toByteBuffer, compression)
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  private def connect() = {
    IO(Tcp) ! Tcp.Connect(remoteAddress = remote)
  }
  private def nextStreamId() = {
    val next = stream = (stream + 1).toShort
    next
  }
  connect()
}

object SessionActor {

  object Protocols {
    case object ReConnect

    case class SetCompression(compression: Compression)

    case class Send[A <: Responses](op: OpCode, body: Option[Body])(implicit val promise: Promise[A])

    sealed trait Operation

    sealed abstract class Requests() extends Operation

    sealed trait Responses extends Operation

    sealed trait Events extends Operation

    case class Startup() extends Requests

    case class AuthResponse(token: Array[Byte]) extends Requests

    case class Options() extends Requests

    case class Query() extends Requests

    case class Prepare() extends Requests

    case class Execute() extends Requests

    case class Batch() extends Requests

    case class Register() extends Requests

    case class Error(code: INT, message: STRING) extends Responses

    case class Ready() extends Responses

    case class Authenticate() extends Responses

    case class Supported(value: STRING_MULTIMAP) extends Responses

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

