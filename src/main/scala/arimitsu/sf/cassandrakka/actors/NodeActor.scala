package arimitsu.sf.cassandrakka.actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging}
import akka.pattern._
import akka.util.Timeout
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols.GetConfig

import scala.collection.mutable
import scala.concurrent.Future

class NodeActor(components: {
  val defaultTimeout: Timeout
  val configurationActor: ActorModule[ConfigurationActor]
  val sessionActor: (InetSocketAddress, Int, ActorModule[NodeActor]) => ActorModule[SessionActor]
}, module: ActorModule[NodeActor], remote: InetSocketAddress) extends Actor with ActorLogging {
  implicit val timeout = components.defaultTimeout
  import context.dispatcher

  val configurationActor = components.configurationActor

  import arimitsu.sf.cassandrakka.actors.NodeActor.Protocols._

  private val sessions = mutable.Map[String, ActorModule[SessionActor]]()

  def receive = {
    case StartAllSession => startAllSession()
    case StartSession(number) => startSession(number)
    case AddSession(id, session) =>
      sessions.put(id, session)
      Future(session).pipeTo(sender())
    case message@Stopped(stoppedRemote, number) if remote.getHostString == stoppedRemote.getHostString =>
      log.warning(s"Connection Actor is Stopped. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
      startSession(number)
    case message => log.warning(s"Unhandled Message. massage: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }

  def startAllSession() = {
    configurationActor.typedAsk(GetConfig).map {
      config =>
        (1 until config.getInt("connection-per-node")).foreach {
          number => startSession(number)
        }
    }
  }

  private def startSession(number: Int) = {
    val session = components.sessionActor(remote, number, module)
    context.self ! AddSession(remote.getHostString + number, session)
  }
}

object NodeActor {

  object Protocols {

    case object StartAllSession

    case class StartSession(number: Int)

    case class AddSession(id: String, connection: ActorModule[SessionActor])

    case class Stopped(stoppedRemote: InetSocketAddress, number: Int)

  }

}