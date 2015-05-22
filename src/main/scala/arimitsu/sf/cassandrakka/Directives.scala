package arimitsu.sf.cassandrakka

import directives._

trait Directives extends AnyRef
  with PrepareDirective
  with ExecuteDirective
  with CompleteDirective {
}

object Directives extends Directives
