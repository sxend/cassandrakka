package arimitsu.sf.cassandrakka.cql

import java.net.{InetSocketAddress, InetAddress}

/**
    [int]          A 4 bytes integer
    [long]         A 8 bytes integer
    [short]        A 2 bytes unsigned integer
    [string]       A [short] n, followed by n bytes representing an UTF-8 string.
    [long string]  An [int] n, followed by n bytes representing an UTF-8 string.
    [uuid]         A 16 bytes long uuid.
    [string list]  A [short] n, followed by n [string].
    [bytes]        A [int] n, followed by n bytes if n >= 0. If n < 0,
                   no byte should follow and the value represented is `null`.
    [value]        A [int] n, followed by n bytes if n >= 0.
                   If n == -1 no byte should follow and the value represented is `null`.
                   If n == -2 no byte should follow and the value represented is
                   `not set` not resulting in any change to the existing value.
                   n < -2 is an invalid value and results in an error.
    [short bytes]  A [short] n, followed by n bytes if n >= 0.
    [option]       A pair of <id><value> where <id> is a [short] representing
                   the option id and <value> depends on that option (and can be
                   of size 0). The supported id (and the corresponding <value>)
                   will be described when this is used.
    [option list]  A [short] n, followed by n [option].
    [inet]         An address (ip and port) to a node. It consists of one
                   [byte] n, that represents the address size, followed by n
                   [byte] representing the IP address (in practice n can only be
                   either 4 (IPv4) or 16 (IPv6)), following by one [int]
                   representing the port.
    [consistency]  A consistency level specification. This is a [short]
                   representing a consistency level with the following
                   correspondance:
                     0x0000    ANY
                     0x0001    ONE
                     0x0002    TWO
                     0x0003    THREE
                     0x0004    QUORUM
                     0x0005    ALL
                     0x0006    LOCAL_QUORUM
                     0x0007    EACH_QUORUM
                     0x0008    SERIAL
                     0x0009    LOCAL_SERIAL
                     0x000A    LOCAL_ONE

    [string map]      A [short] n, followed by n pair <k><v> where <k> and <v>
                      are [string].
    [string multimap] A [short] n, followed by n pair <k><v> where <k> is a
                      [string] and <v> is a [string list].
    [bytes map]       A [short] n, followed by n pair <k><v> where <k> is a
                      [string] and <v> is a [bytes].
 */

sealed abstract class Notation(value: Any)
object Notations {
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
  case class OPTION_LIST(value: List[OPTION]) extends Notation(value)
  case class INET(value: InetSocketAddress) extends Notation(value)
  case class CONSISTENCY(value: notations.Consistency) extends Notation(value)
  case class STRING_MAP(value: Map[STRING, STRING]) extends Notation(value)
  case class STRING_MULTIMAP(value: Map[STRING, STRING_LIST]) extends Notation(value)
  case class BYTES_MAP(value: Map[STRING, BYTES]) extends Notation(value)

  def parseINT (bytes: Array[Byte]): INT = ???
  def parseLONG (bytes: Array[Byte]): LONG = ???
  def parseSHORT (bytes: Array[Byte]): SHORT = ???
  def parseSTRING (bytes: Array[Byte]): STRING = ???
  def parseLONG_STRING (bytes: Array[Byte]): LONG_STRING = ???
  def parseUUID (bytes: Array[Byte]): UUID = ???
  def parseSTRING_LIST (bytes: Array[Byte]): STRING_LIST = ???
  def parseBYTES (bytes: Array[Byte]): BYTES = ???
  def parseVALUE (bytes: Array[Byte]): VALUE = ???
  def parseSHORT_BYTES (bytes: Array[Byte]): SHORT_BYTES = ???
  def parseOPTION (bytes: Array[Byte]): OPTION = ???
  def parseOPTION_LIST (bytes: Array[Byte]): OPTION_LIST = ???
  def parseINET (bytes: Array[Byte]): INET = ???
  def parseCONSISTENCY (bytes: Array[Byte]): CONSISTENCY = ???
  def parseSTRING_MAP (bytes: Array[Byte]): STRING_MAP = ???
  def parseSTRING_MULTIMAP (bytes: Array[Byte]): STRING_MULTIMAP = ???
  def parseBYTES_MAP (bytes: Array[Byte]): BYTES_MAP = ???
}
