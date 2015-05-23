package arimitsu.sf.cassandrakka

import akka.actor.ActorSystem

object UsageExample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("example-system")
    new UsageExample(new {
      implicit val system = ActorSystem("example-system")
      val cassandrakka: Cassandrakka = Cassandrakka()
    }).main()
  }
}

class UsageExample(components: {
  val system: ActorSystem
  val cassandrakka: Cassandrakka
}) {

  import Directives._
  import components.system.dispatcher
  import components.cassandrakka._

  def main() = {
    val future = withSession { implicit session =>
      prepare("select * from hello_world where id = '?'") {
        id =>
          println(id)
          "OK"
      }
    }

    future onSuccess {
      case result =>
        println(result)
    }

  }
}
