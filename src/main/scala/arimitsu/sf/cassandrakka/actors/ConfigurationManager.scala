package arimitsu.sf.cassandrakka.actors

import akka.actor.Actor
import com.typesafe.config.{ConfigFactory, Config}

class ConfigurationManager extends Actor {
  var configuration: Config = ConfigFactory.load
  def receive = {
    case _ =>
  }
}
