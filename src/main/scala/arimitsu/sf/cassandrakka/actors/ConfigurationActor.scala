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
  import ConfigurationActor.Protocols._
  def receive = {
    case GetConfig => configuration
    case message: GetCompression =>
      import GetCompressions._
      message.reply(Future {
        Compressions.valueOf(configuration.getString("compression"))
      }).typedPipeTo(sender())
    case message: GetGlobalTimeout =>
      import GetGlobalTimeouts._
    message.reply(Future {
      configuration.getInt("global-timeout")
    }).typedPipeTo(sender())
    case message: WithValue =>
      import WithValues._
      message.reply(Future {
        import message._
      configuration = configuration.withValue(name, value)
      configuration
    }).typedPipeTo(sender())
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }
}

object ConfigurationActor {

  object Protocols {

    case class WithValue(name: String, value: ConfigValue)

    case class GetConfig()

    case class GetGlobalTimeout()

    case class GetCompression()

    implicit val GetCompressions = Mapping[ConfigurationActor, GetCompression, Compression]

    implicit val WithValues = Mapping[ConfigurationActor, WithValue, Config]

    implicit val GetConfigs = Mapping[ConfigurationActor, GetConfig, Config]

    implicit val GetGlobalTimeouts = Mapping[ConfigurationActor, GetGlobalTimeout, Int]
  }

}