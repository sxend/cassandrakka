package arimitsu.sf.cassandrakka.cql

sealed abstract class Version(val code: Byte)

class Versions {

  case object REQUEST extends Version(0x04.toByte)

  case object RESPONSE extends Version(0x84.toByte)

}
