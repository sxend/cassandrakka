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
    import ExecutionContext.Implicits.global
    val future: Future[String] = withSession { implicit session =>
      prepare("select * from test where id = ?") {
        id => execute(id, "1") {
          result =>
            println(result)
            "OK"
        }
      }
    }.future

    future.onComplete {
      case Success(a) => a == "OK"
      case _ => ()
    }
  }
}

case class Test(id: String, value: String)