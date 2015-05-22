package arimitsu.sf.cassandrakka

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.util.Timeout
import arimitsu.sf.cassandrakka._
import arimitsu.sf.cassandrakka.actors.{ConfigurationManager, ClusterManager}
import arimitsu.sf.cassandrakka.directives._
import com.typesafe.config.Config
import akka.pattern.ask
import scala.concurrent.Future

object Cassandrakka {
  import ClusterManager.Protocol._
  def apply(config: Option[Config] = None)(implicit system: ActorSystem): Cassandrakka = {
    val _system = system
    new Cassandrakka {
      override private[cassandrakka] val components: Components = new Components() {
        override implicit val system: ActorSystem = _system
      }
      import components.system.dispatcher
      import Directives._
      override def withSession[A](op: => Op[A]): Future[A] = {
        implicit val timeout = Timeout(10, TimeUnit.SECONDS)
        components.clusterManager.toActorRef.ask(GetSession).mapTo[Session].flatMap(session => op(session))
      }
    }
  }
}
trait Cassandrakka {
  private[cassandrakka] val components: Components
  def withSession[A](op: => Op[A]): Future[A]
}