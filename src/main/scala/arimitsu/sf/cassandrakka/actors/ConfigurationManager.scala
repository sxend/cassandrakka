package arimitsu.sf.cassandrakka.actors

import akka.actor.{Actor, ActorLogging}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.ActorModule.Mapping
import com.typesafe.config.{Config, ConfigFactory, ConfigValue}

import scala.concurrent.Future

class ConfigurationManager(components: {
  val defaultConfiguration: Option[Config]
}, module: ActorModule[ConfigurationManager]) extends Actor with ActorLogging {

  import ConfigurationManager.Protocols._
  import context.dispatcher

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