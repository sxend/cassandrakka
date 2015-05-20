package arimitsu.sf.cassandrakka

import arimitsu.sf.cassandrakka.directives.{CompleteDirective, ExecuteDirective, PrepareDirective}

object Directives extends AnyRef
  with PrepareDirective
  with ExecuteDirective
  with CompleteDirective {
}
