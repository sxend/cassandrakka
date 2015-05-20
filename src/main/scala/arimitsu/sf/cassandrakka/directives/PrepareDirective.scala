package arimitsu.sf.cassandrakka.directives

import arimitsu.sf.cassandrakka.Session

import scala.concurrent.{Promise, Future}

trait PrepareDirective {
  self =>

  def prepare(query: String): Directive1[Byte] = new Directive1[Byte] {
    override def apply[Z](child: => (Byte) => Future[Z])(implicit session: Session): Future[Z] = {
      import session.ec
      session.prepare(query).flatMap(id => child(id))
    }
  }
}
