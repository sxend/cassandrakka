package arimitsu.sf.cassandrakka

import akka.actor.ActorSystem

import scala.concurrent.Future

object DriverManager {
  def getDriver(implicit system: ActorSystem): Driver = {
    new Driver {
      override def getSession: Session = ???
    }
  }
}

