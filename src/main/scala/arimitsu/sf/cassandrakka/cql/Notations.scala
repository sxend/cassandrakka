package arimitsu.sf.cassandrakka.cql

sealed trait Notation
object Notations {
  def fromBytes(bytes: Array[Byte]): Notation = {
    ???
  }
}
