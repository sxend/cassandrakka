package arimitsu.sf.cassandrakka.actors

import akka.actor.{Actor, ActorLogging}
import arimitsu.sf.cassandrakka.ActorModule
import arimitsu.sf.cassandrakka.ActorModule._
import arimitsu.sf.cassandrakka.cql.{Compressions, Compression}
import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import akka.pattern._

import scala.concurrent.Future

class ConfigurationActor(components: {
  val defaultConfiguration: Option[Config]
}, module: ActorModule[ConfigurationActor]) extends Actor with ActorLogging {

  import ConfigurationActor.Protocols._
  import context.dispatcher

  var configuration: Config = components.defaultConfiguration.getOrElse(ConfigFactory.load.getConfig("arimitsu.sf.cassandrakka"))
  import arimitsu.sf.cassandrakka.actors.ConfigurationActor.Protocols._
  def receive = {
    case GetConfig => configuration
    case message: GetCompression =>
      import message._
      Future {
        Compressions.valueOf(configuration.getString("compression"))
      }.typedPipeTo(sender())
    case GetGlobalTimeout =>
      Future(configuration.getInt("global-timeout")).typedPipeTo(sender())
    case WithValue(name, value) =>
      val after = configuration = configuration.withValue(name, value)
      Future(after)
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }
}

object ConfigurationActor {

  object Protocols {

    case class WithValue(name: String, value: ConfigValue)

    case class GetConfig()

    case class GetGlobalTimeout()

    case class GetCompression() extends Mapping[ConfigurationActor, GetCompression, Compression]

    implicit object WithValueResult extends Mapping[ConfigurationActor, WithValue, Config]

    implicit object GetConfigMapping extends Mapping[ConfigurationActor, GetConfig, Config]

    implicit object GetGlobalTimeoutMapping extends Mapping[ConfigurationActor, GetGlobalTimeout, Int]

  }

}