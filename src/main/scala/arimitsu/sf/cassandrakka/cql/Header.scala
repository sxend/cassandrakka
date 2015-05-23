package arimitsu.sf.cassandrakka.cql

case class Header(version: Version, flag: Flag, stream: Stream, opCode: OpCode)
