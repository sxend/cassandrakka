package arimitsu.sf.cassandrakka.cql

import java.nio.ByteBuffer

case class Frame(header: Header, body: Body)

object Frame {
  def parse(buffer: ByteBuffer, compression: Compression) = {
    val header = Header.parse(buffer)
    val body = Body.parse(header.opCode, buffer, compression, header.flag.is(Flags.COMPRESSION))
    Frame(header, body)
  }
}