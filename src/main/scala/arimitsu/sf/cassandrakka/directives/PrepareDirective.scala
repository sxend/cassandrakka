package arimitsu.sf.cassandrakka.directives

import arimitsu.sf.cassandrakka._

import scala.concurrent.Future

trait PrepareDirective {
  self =>
  //  def prepare(magnet: PrepareMagnet): PrepareMagnet = magnet
  def prepare(query: => String): PrepareMagnet = query

  implicit class PrepareMagnet(query: => String) {
    def apply[A](f: => Byte => A)(implicit session: Session): Future[A] = {
      import session.ec
      Future("1".getBytes.head).map(f)
    }
  }

}

object PrepareDirective extends PrepareDirective

//
//trait PrepareMagnet {
//  type Out <: HList
//
//  def apply(f: => Byte => Out): Future[Out]
//}
//
//object PrepareMagnet {
//
//  implicit def apply[T](query: => String)(implicit hl: HListable[T], session: Session) = new Directive1[hl.Out] with PrepareMagnet {
//      import session.ec
//      type Out = hl.Out
//
//    override def apply(f: => (Byte) => Out): Future[Out] = {
//      session.prepare(query).map(id => f(id))
//    }
//  }
//}

