package arimitsu.sf

import shapeless._

import scala.concurrent.Future

package object cassandrakka {
  type Op[A] = Session => Future[A]
  type OpGenerator[T] = T => Op
  type Directive0 = Directive[HNil]
  type Directive1[T] = Directive1[T :: HNil]

  trait HListable[T] {
    type Out <: HList

    def apply(value: T): Out
  }

  object HListable extends LowerPriorityHListable {
    implicit def fromHList[T <: HList] = new HListable[T] {
      type Out = T

      def apply(value: T) = value
    }
  }

  abstract class LowerPriorityHListable {
    implicit def fromAnyRef[T] = new HListable[T] {
      type Out = T :: HNil

      def apply(value: T) = value :: HNil
    }
  }

}
