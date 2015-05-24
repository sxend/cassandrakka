package arimitsu.sf.cassandrakka.cql

import net.jpountz.lz4.LZ4Factory

sealed abstract class Compression(val name: String) {
  def compress(bytes: Array[Byte], length: Int = 0): Array[Byte]

  def decompress(bytes: Array[Byte], length: Int = 0): Array[Byte]
}

object Compressions {

  case object LZ4 extends Compression("lz4") {
    override def compress(bytes: Array[Byte], length: Int): Array[Byte] = {
      val factory = LZ4Factory.fastestInstance()
      val compressor = factory.fastCompressor()
      compressor.compress(bytes)
    }

    override def decompress(bytes: Array[Byte], length: Int): Array[Byte] = {
      val factory = LZ4Factory.fastestInstance()
      val decompressor = factory.fastDecompressor()
      decompressor.decompress(bytes, length)
    }
  }

  case object Snappy extends Compression("snappy") {
    override def compress(bytes: Array[Byte], length: Int = 0): Array[Byte] = {
      org.xerial.snappy.Snappy.compress(bytes)
    }

    override def decompress(bytes: Array[Byte], length: Int = 0): Array[Byte] = {
      org.xerial.snappy.Snappy.uncompress(bytes)
    }
  }

  case object Raw extends Compression("raw") {
    override def compress(bytes: Array[Byte], length: Int = 0): Array[Byte] = bytes

    override def decompress(bytes: Array[Byte], length: Int = 0): Array[Byte] = bytes
  }

}
