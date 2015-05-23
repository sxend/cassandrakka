package arimitsu.sf.cassandrakka

import akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

object UsageExample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("example-system")
    new UsageExample(new {
      val cassandrakka: Cassandrakka = Cassandrakka()
    }).main()
  }
}

class UsageExample(components: {
  val cassandrakka: Cassandrakka
}) {
  import Directives._
  import components.cassandrakka._
  def main() = {


  }
}
