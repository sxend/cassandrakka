package arimitsu.sf.cassandrakka

import akka.actor.{ActorRef, Props, ActorSystem}
import arimitsu.sf.cassandrakka.actors.SessionActor
import arimitsu.sf.cassandrakka.cql.messages._

import scala.concurrent.Future

trait Driver {
  self =>
  def getSession: Session
}
