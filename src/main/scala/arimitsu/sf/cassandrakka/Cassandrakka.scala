package arimitsu.sf.cassandrakka

import akka.actor.{Props, ActorSystem}
import arimitsu.sf.cassandrakka.actors.{ConfigurationManager, ClusterManager}

object Cassandrakka{
  def apply()(implicit system: ActorSystem): Cassandrakka = {
    val confugurationManager = system.actorOf(Props(classOf[ConfigurationManager]))
    system.actorOf(Props(classOf[ClusterManager], new {
      val configurationManager = configurationManager
    }))
    new Cassandrakka {
      override def withCql(f: => (Session) => Unit): Unit = {

      }
    }
  }
}
trait Cassandrakka {
  def withCql(f: => Session => Unit): Unit
}
