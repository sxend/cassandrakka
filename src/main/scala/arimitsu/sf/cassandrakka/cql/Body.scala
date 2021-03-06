package arimitsu.sf.cassandrakka.cql

import java.nio.ByteBuffer

import arimitsu.sf.cassandrakka.cql.Compressions.Raw

case class Body(opCode: OpCode, bytes: Array[Byte], length: Int, compression: Compression = Raw, compressed: Boolean = false) {
  def compress(compression: Compression): Body = compressed match {
    case true => Body(opCode, compression.compress(this.bytes, length), length, compressed = true)
    case false => this
  }

  def decompress(compression: Compression): Body = compressed match {
    case true => Body(opCode, compression.decompress(this.bytes, length), length, compressed = false)
    case false => this
  }

}

object Body {
  def parse(opCode: OpCode, buffer: ByteBuffer, compression: Compression, compressed: Boolean): Body = {
    val length = buffer.getInt
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    Body(opCode, bytes, length, compression, compressed)
  }
}