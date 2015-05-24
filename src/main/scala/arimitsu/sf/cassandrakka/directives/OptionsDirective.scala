package arimitsu.sf.cassandrakka.directives

import arimitsu.sf.cassandrakka.Session

import scala.concurrent.Future

trait OptionsDirective {
  def options: OptionsMagnet = ()

  implicit class OptionsMagnet(unit: => ()) {
    def apply[A](f: => Supported => A)(implicit session: Session): Future[A] = {
      session.options().flatMap{
        result => result.right.get
      }
    }
  }
}

