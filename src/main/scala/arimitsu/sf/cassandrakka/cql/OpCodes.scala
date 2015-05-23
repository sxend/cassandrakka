package arimitsu.sf.cassandrakka.cql

sealed abstract class OpCode(val code: Byte)

object OpCodes {
  case object ERROR extends OpCode(0x00.toByte)
  case object STARTUP extends OpCode(0x01.toByte)
  case object READY extends OpCode(0x02.toByte)
  case object AUTHENTICATE extends OpCode(0x03.toByte)
  case object OPTIONS extends OpCode(0x05.toByte)
  case object SUPPORTED extends OpCode(0x06.toByte)
  case object QUERY extends OpCode(0x07.toByte)
  case object RESULT extends OpCode(0x08.toByte)
  case object PREPARE extends OpCode(0x09.toByte)
  case object EXECUTE extends OpCode(0x0A.toByte)
  case object REGISTER extends OpCode(0x0B.toByte)
  case object EVENT extends OpCode(0x0C.toByte)
  case object BATCH extends OpCode(0x0D.toByte)
  case object AUTH_CHALLENGE extends OpCode(0x0E.toByte)
  case object AUTH_RESPONSE extends OpCode(0x0F.toByte)
  case object AUTH_SUCCESS extends OpCode(0x10.toByte)

}
