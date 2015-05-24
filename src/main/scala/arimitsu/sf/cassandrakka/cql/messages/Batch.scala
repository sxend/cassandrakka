package arimitsu.sf.cassandrakka.cql.messages

case class Batch(`type`: Byte, flags: Byte, queries: List[Query], consistency: Consistency)
