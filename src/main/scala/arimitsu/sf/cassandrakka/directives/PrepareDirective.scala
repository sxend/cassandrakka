package arimitsu.sf.cassandrakka.directives

import shapeless._
import arimitsu.sf.cassandrakka._

import scala.concurrent.Future

trait PrepareDirective {
  self =>
  def prepare(magnet: PrepareMagnet): Directive1[Byte] = magnet.get
}

object PrepareDirective extends PrepareDirective

trait PrepareMagnet {
  type Out <: HList
  def get: Directive1[Out]
}

object PrepareMagnet {
  implicit def apply[T](query: => String)(implicit hl: HListable[T], session: Session) = new Directive1[hl.Out] with PrepareMagnet {

    }
}

