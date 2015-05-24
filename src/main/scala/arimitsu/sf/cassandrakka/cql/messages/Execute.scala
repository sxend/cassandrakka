package arimitsu.sf.cassandrakka.cql.messages

case class Execute(id: Array[Byte], queryParameter: Option[String])
