package arimitsu.sf.cassandrakka.directives

trait CompleteDirective {
  //  def complete[A](result: A): Directive[A] = new Directive[A] {
  //
  //    override def future[Z](implicit f: (A) => Z): Future[Z] = {
  //      val promise = Promise[Z]()
  //      promise.success(f(result))
  //      promise.future
  //    }
  //
  //  }
}
