package arimitsu.sf.cassandrakka

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.util.Timeout
import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols.GetGlobalTimeout
import arimitsu.sf.cassandrakka.actors._
import com.typesafe.config.Config

import scala.concurrent.Future

object Cassandrakka {

  import ClusterActor.Protocols._

  def apply(config: Option[Config] = None)(implicit system: ActorSystem): Cassandrakka = {
    val _system = system
    new Cassandrakka {
      override private[cassandrakka] val components: Components = new Components() {
        override implicit val system: ActorSystem = _system
      }

      import components.system.dispatcher

      override def withSession[A](op: => Op[A]): Future[A] = {
        implicit val t = Timeout(1000, TimeUnit.MICROSECONDS)
        import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols._
        val future = for {
          timeout <- components.configurationActor.typedAsk(GetGlobalTimeout())
          session <- components.clusterActor.typedAsk(GetSession)
        } yield (Timeout(timeout, TimeUnit.MICROSECONDS), session)
        future flatMap {
          result =>
            implicit val timeout = result._1
            val sessionActor = result._2
            op(new Session(components, sessionActor) {})
        }
      }
    }
  }
}

trait Cassandrakka {
  private[cassandrakka] val components: Components

  def withSession[A](op: => Op[A]): Future[A]
}
