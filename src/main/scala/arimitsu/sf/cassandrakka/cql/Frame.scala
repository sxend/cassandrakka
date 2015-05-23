package arimitsu.sf.cassandrakka.cql

case class Frame(header: Header, body: Body){
  def length: Int = body.bytes.length
}
