package arimitsu.sf.cassandrakka.cql

import arimitsu.sf.cassandrakka.cql.Compressions.Raw

case class Body(bytes: Array[Byte], length: Int, compression: Compression = Raw, compressed: Boolean = false){
  def compress(compression: Compression): Body = compressed match {
    case true => Body(compression.compress(this.bytes, length), length, compressed = true)
    case false => this
  }
  def decompress(compression: Compression): Body = compressed match {
    case true => Body(compression.decompress(this.bytes, length), length, compressed = false)
    case false => this
  }

  def toNotation: Notation = compressed match {
    case true => Body(compression.decompress(bytes), length).toNotation
    case false => Notations.fromBytes(bytes)
  }

}

object Body{
  def fromBytes(bytes: Array[Byte], length: Int, compression: Compression = Raw, isCompression: Boolean = false): Body = {
    Body(bytes, length, compression, isCompression)
  }
}