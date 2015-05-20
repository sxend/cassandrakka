package arimitsu.sf.cassandrakka.directives

import arimitsu.sf.cassandrakka.Session

import scala.concurrent.{Future}

trait Directive[A] {
  def future[Z](implicit f: A => Z): Future[Z]
}
trait Directive0 {
  self =>
  def apply[Z](implicit session: Session): Future[Z]
}

trait Directive1[A] {
  self =>
  def apply[Z](child: => A => Future[Z])(implicit session: Session): Future[Z]
}
