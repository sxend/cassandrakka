package arimitsu.sf.cassandrakka.actors

import akka.actor.{ActorLogging, Actor}
import arimitsu.sf.cassandrakka.ActorModule.Mapping
import arimitsu.sf.cassandrakka.actors.ConfigurationManager.Protocols.GetConfig
import com.typesafe.config.{ConfigValue, ConfigFactory, Config}

import scala.concurrent.Future

class ConfigurationManager(components: {
  val defaultConfiguration: Option[Config]
}) extends Actor with ActorLogging {
  import context.dispatcher
  import ConfigurationManager.Protocols._
  var configuration: Config = components.defaultConfiguration.getOrElse(ConfigFactory.load.getConfig("arimitsu.sf.cassandrakka"))

  def receive = {
    case GetConfig => configuration
    case WithValue(name, value) =>
      val after = configuration = configuration.withValue(name, value)
      Future(after)
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }
}

object ConfigurationManager {

  object Protocols {
    case class WithValue(name: String, value: ConfigValue)
    implicit object WithValueResult extends Mapping[ConfigurationManager, WithValue, Config]
    implicit case object GetConfig extends Mapping[ConfigurationManager, GetConfig.type, Config]

  }

}