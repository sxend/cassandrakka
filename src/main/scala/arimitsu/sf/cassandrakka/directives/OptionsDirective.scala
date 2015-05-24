package arimitsu.sf.cassandrakka.directives

import arimitsu.sf.cassandrakka.Session
import arimitsu.sf.cassandrakka.cql.messages.Supported

import scala.concurrent.Future

trait OptionsDirective {
  def options: OptionsMagnet = ()

  implicit class OptionsMagnet(unit: Unit) {
    def apply[A](f: => Supported => A)(implicit session: Session): Future[A] = {
      ???
//      session.options().map {
//        result =>
//          val g = result.right.get.value.value.map(v => v._1.value -> v._2.value.map(l => l.value))
//          f(Supported(g))
//      }
    }
  }
}

