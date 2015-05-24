package arimitsu.sf.cassandrakka.cql

sealed abstract class Version(val code: Byte)

object Versions {

  case object REQUEST extends Version(0x04.toByte)

  case object RESPONSE extends Version(0x84.toByte)

  def valueOf(code: Byte): Version = code match {
    case REQUEST.code => REQUEST
    case RESPONSE.code => RESPONSE
  }
}
