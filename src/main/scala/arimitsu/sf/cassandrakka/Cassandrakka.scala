package arimitsu.sf.cassandrakka

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import arimitsu.sf.cassandrakka.actors._
import com.typesafe.config.Config

import scala.concurrent.Future

object Cassandrakka {

  import ClusterActor.Protocol._

  def apply(config: Option[Config] = None)(implicit system: ActorSystem): Cassandrakka = {
    val _system = system
    new Cassandrakka {
      override private[cassandrakka] val components: Components = new Components() {
        override implicit val system: ActorSystem = _system
      }

      import components.system.dispatcher

      override def withSession[A](op: => Op[A]): Future[A] = {
        implicit val timeout = Timeout(10, TimeUnit.SECONDS)
        components.clusterActor.actorRef.ask(GetSession).mapTo[Session].flatMap(session => op(session))
      }
    }
  }
}

trait Cassandrakka {
  private[cassandrakka] val components: Components

  def withSession[A](op: => Op[A]): Future[A]
}
