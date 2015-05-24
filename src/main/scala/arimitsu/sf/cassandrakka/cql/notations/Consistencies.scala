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
    case 0x0000 => ANY
    case 0x0001 => ONE
    case 0x0002 => TWO
    case 0x0003 => THREE
    case 0x0004 => QUORUM
    case 0x0005 => ALL
    case 0x0006 => LOCAL_QUORUM
    case 0x0007 => EACH_QUORUM
    case 0x0008 => SERIAL
    case 0x0009 => LOCAL_SERIAL
    case 0x000A => LOCAL_ONE
  }
}
