package arimitsu.sf.cassandrakka.cql.messages

import arimitsu.sf.cassandrakka.cql.notations.Consistency

case class Batch(`type`: Byte, flags: Byte, queries: List[Query], consistency: Consistency)
