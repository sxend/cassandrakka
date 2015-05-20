package arimitsu.sf.cassandrakka.actors

import akka.actor.Actor
import com.typesafe.config.{ConfigFactory, Config}

class ConfigurationManager(components: {
  val defaultConfiguration: Option[Config]
}) extends Actor {
  var configuration: Config = components.defaultConfiguration.getOrElse(ConfigFactory.load)
  def receive = {
    case _ =>
  }
}
