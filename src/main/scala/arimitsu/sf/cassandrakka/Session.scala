package arimitsu.sf.cassandrakka

import scala.concurrent.{ExecutionContext, Future}

trait Session {
  implicit val ec: ExecutionContext

//  def prepare[A](query: String): Future[Byte]
//
//  def execute(id: Byte, arg: Any*): Future[AnyRef]
}

