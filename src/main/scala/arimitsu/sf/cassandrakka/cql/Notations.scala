package arimitsu.sf.cassandrakka.cql

import java.net.{InetAddress, InetSocketAddress}
import java.nio.ByteBuffer
import java.nio.charset.Charset

import arimitsu.sf.cassandrakka.cql.notations.Consistencies

sealed abstract class Notation(value: Any)
object Notations {
  private val `UTF-8` = Charset.forName("UTF-8")
  case class INT(value: Int) extends Notation(value)
  case class LONG(value: Long) extends Notation(value)
  case class SHORT(value: Short) extends Notation(value)
  case class STRING(value: String) extends Notation(value)
  case class LONG_STRING(value: String) extends Notation(value)
  case class UUID(value: java.util.UUID) extends Notation(value)
  case class STRING_LIST(value: List[STRING]) extends Notation(value)
  case class BYTES(value: Option[Array[Byte]]) extends Notation(value)
  case class VALUE(value: Either[NOT_SET.type , Option[Array[Byte]]]) extends Notation(value)
  case object NOT_SET
  case class SHORT_BYTES(value: Array[Byte]) extends Notation(value)
  case class OPTION(id: SHORT, value: Option[Notation]) extends Notation(value)
  case class OPTION_LIST(length: SHORT, value: List[OPTION]) extends Notation(value)
  case class INET(value: InetSocketAddress) extends Notation(value)
  case class CONSISTENCY(value: notations.Consistency) extends Notation(value)
  case class STRING_MAP(value: Map[STRING, STRING]) extends Notation(value)
  case class STRING_MULTIMAP(value: Map[STRING, STRING_LIST]) extends Notation(value)
  case class BYTES_MAP(value: Map[STRING, BYTES]) extends Notation(value)

  def parseINT (buffer: ByteBuffer): INT = INT(buffer.getInt)
  def parseLONG (buffer: ByteBuffer): LONG = LONG(buffer.getLong)
  def parseSHORT (buffer: ByteBuffer): SHORT = SHORT(buffer.getShort)
  def parseSTRING (buffer: ByteBuffer): STRING = {
    val length = parseSHORT(buffer).value
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    STRING(new String(bytes, `UTF-8`))
  }
  def parseLONG_STRING (buffer: ByteBuffer): LONG_STRING = {
    val length = parseINT(buffer).value
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    LONG_STRING(new String(bytes, `UTF-8`))
  }
  private val UUID_CONSTRUCTOR = {
    val c = classOf[java.util.UUID].getDeclaredConstructor(classOf[Array[Byte]])
    c.setAccessible(true)
    c
  }
  def parseUUID (buffer: ByteBuffer): UUID = {
    val bytes = new Array[Byte](16)
    buffer.get(bytes)
    UUID(UUID_CONSTRUCTOR.newInstance(bytes))
  }
  def parseSTRING_LIST (buffer: ByteBuffer): STRING_LIST = {
    val length = parseSHORT(buffer).value
    STRING_LIST((0 until length).map{
      _ => parseSTRING(buffer)
    }.toList)
  }
  def parseBYTES (buffer: ByteBuffer): BYTES = {
    val length = parseINT(buffer)
    length.value match {
      case i if i < 0 => BYTES(None)
      case i =>
        val bytes = new Array[Byte](i)
        buffer.get(bytes)
        BYTES(Option(bytes))
    }
  }
  def parseVALUE (buffer: ByteBuffer): VALUE = {
    val length = buffer.getInt
    length match {
      case i if i == -1 => VALUE(Right(None))
      case i if i == -2 => VALUE(Left(NOT_SET))
      case i =>
        val bytes = new Array[Byte](i)
        buffer.get(bytes)
        VALUE(Right(Option(bytes)))
    }
  }
  def parseSHORT_BYTES (buffer: ByteBuffer): SHORT_BYTES = {
    val length = buffer.getShort
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    SHORT_BYTES(bytes)
  }
  def parseOPTION (buffer: ByteBuffer): OPTION = {
    val id = parseSHORT(buffer)
    OPTION(id, Option(parseOPTION(buffer)))
  }
  def parseOPTION_LIST (buffer: ByteBuffer): OPTION_LIST = {
    val length = parseSHORT(buffer)
    OPTION_LIST(length, (0 until length.value).map{
      _ => parseOPTION(buffer)
    }.toList)
  }
  def parseINET (buffer: ByteBuffer): INET = {
    val length = buffer.get().toInt
    val bytes = new Array[Byte](length)
    buffer.get(bytes)
    val port = parseINT(buffer)
    INET(new InetSocketAddress(InetAddress.getByAddress(bytes), port)) // FIXME
  }
  def parseCONSISTENCY (buffer: ByteBuffer): CONSISTENCY = {
    CONSISTENCY(Consistencies.fromShorts(parseSHORT(buffer).value))
  }
  def parseSTRING_MAP (buffer: ByteBuffer): STRING_MAP = {
    STRING_MAP({
      val length = parseSHORT(buffer).value
      (0 until length).map{
        _ => parseSTRING(buffer) -> parseSTRING(buffer)
      }.toMap
    })
  }
  def parseSTRING_MULTIMAP (buffer: ByteBuffer): STRING_MULTIMAP = {
    STRING_MULTIMAP(// FIXME
    )
  }
  def parseBYTES_MAP (buffer: ByteBuffer): BYTES_MAP = {
    val length = buffer.getShort  // FIXME
  }
}
