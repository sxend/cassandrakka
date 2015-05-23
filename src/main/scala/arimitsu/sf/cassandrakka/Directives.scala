package arimitsu.sf.cassandrakka

import arimitsu.sf.cassandrakka.directives._

trait Directives extends AnyRef
with PrepareDirective
with ExecuteDirective
with CompleteDirective {
}

object Directives extends Directives
