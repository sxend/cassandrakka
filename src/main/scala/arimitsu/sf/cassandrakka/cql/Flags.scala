package arimitsu.sf.cassandrakka.cql

sealed abstract class Flag(val mask: Byte) {
  self =>
  def |(flag: Flag) = new Flag((self.mask | flag.mask).toByte) {}
  def is(flag: Flag): Boolean = (flag.mask & self.mask) > 0
}

object Flags {

  case object COMPRESSION extends Flag(0x01.toByte)

  case object TRACING extends Flag(0x02.toByte)

  case object CUSTOM extends Flag(0x04.toByte)

  case object WARNING extends Flag(0x08.toByte)

  def valueOf(mask: Byte): Flag = new Flag(mask){}

}
