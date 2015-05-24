package arimitsu.sf.cassandrakka.cql

import java.nio.ByteBuffer

case class Header(version: Version, flag: Flag, stream: Stream, opCode: OpCode)

object Header{
  def parse(buffer: ByteBuffer): Header = {
    val version = buffer.get()
    val flag = buffer.get()
    val stream = buffer.getShort
    val opCode = buffer.get()
    Header(Versions.valueOf(version), Flags.valueOf(flag), Stream(stream), OpCodes.valueOf(opCode))
  }
}