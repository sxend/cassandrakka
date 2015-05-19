package arimitsu.sf.cassandrakka

import akka.actor.ActorSystem

object UsageExample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("example-system")
    val cassandrakka = Cassandrakka()
    new UsageExample(new {
      val cassandrakka: Cassandrakka = cassandrakka
    }).main()
  }
}

class UsageExample(components: {
  val cassandrakka: Cassandrakka
}) {
  import components.cassandrakka._
  def main() = {
    withCql { implicit session =>
      
    }
  }
}