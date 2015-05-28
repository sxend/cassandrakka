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
    case message: GetCompression => Mapping(message) {
      Future {
        Compressions.valueOf(configuration.getString("compression"))
      }
    }.typedPipeTo(sender())
    case message: GetGlobalTimeout => Mapping(message){
      Future(configuration.getInt("global-timeout"))
      }.typedPipeTo(sender())
    case message: WithValue => Mapping(message) {
      import message._
      configuration = configuration.withValue(name, value)
      Future(configuration)
    }.typedPipeTo(sender())
    case message => log.warning(s"Unhandled Message. message: $message, sender: ${sender().toString()}, self: ${self.toString()}")
  }
}

object ConfigurationActor {

  object Protocols {

    case class WithValue(name: String, value: ConfigValue)

    case class GetConfig()

    case class GetGlobalTimeout()

    case class GetCompression()

    implicit object GetCompression extends Mapping[ConfigurationActor, GetCompression, Compression]

    implicit object WithValue extends Mapping[ConfigurationActor, WithValue, Config]

    implicit object GetConfig extends Mapping[ConfigurationActor, GetConfig, Config]

    implicit object GetGlobalTimeout extends Mapping[ConfigurationActor, GetGlobalTimeout, Int]

  }

}