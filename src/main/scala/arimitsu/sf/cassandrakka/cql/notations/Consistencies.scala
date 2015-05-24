package arimitsu.sf.cassandrakka.cql.notations

sealed abstract class Consistency(val level: Short)

object Consistencies {

  case object ANY extends Consistency(0x0000.toShort)

  case object ONE extends Consistency(0x0001.toShort)

  case object TWO extends Consistency(0x0002.toShort)

  case object THREE extends Consistency(0x0003.toShort)

  case object QUORUM extends Consistency(0x0004.toShort)

  case object ALL extends Consistency(0x0005.toShort)

  case object LOCAL_QUORUM extends Consistency(0x0006.toShort)

  case object EACH_QUORUM extends Consistency(0x0007.toShort)

  case object SERIAL extends Consistency(0x0008.toShort)

  case object LOCAL_SERIAL extends Consistency(0x0009.toShort)

  case object LOCAL_ONE extends Consistency(0x000A.toShort)

  def fromShorts(s: Short): Consistency = s match {
    case ANY.level => ANY
    case ONE.level => ONE
    case TWO.level => TWO
    case THREE.level => THREE
    case QUORUM.level => QUORUM
    case ALL.level => ALL
    case LOCAL_QUORUM.level => LOCAL_QUORUM
    case EACH_QUORUM.level => EACH_QUORUM
    case SERIAL.level => SERIAL
    case LOCAL_SERIAL.level => LOCAL_SERIAL
    case LOCAL_ONE.level => LOCAL_ONE
  }
}
