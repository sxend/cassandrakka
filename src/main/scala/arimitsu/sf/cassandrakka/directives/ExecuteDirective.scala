package arimitsu.sf.cassandrakka.directives

trait ExecuteDirective {
  self =>

  //  def execute(id: Byte, arg: Any*): Directive1[AnyRef] = new Directive1[AnyRef] {
  //    override def apply[Z](child: => (AnyRef) => Future[Z])(implicit session: Session): Future[Z] = {
  //      import session.ec
  //      session.execute(id, arg).flatMap(result => child(result))
  //    }
  //  }
}
