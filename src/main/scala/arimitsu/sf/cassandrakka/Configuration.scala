package arimitsu.sf.cassandrakka

import com.typesafe.config.ConfigFactory

trait Configuration {
  private val _configuration = ConfigFactory.load.getConfig("arimitsu.sf.cassandrakka")
  def nodes = _configuration
}

object Configuration extends Configuration