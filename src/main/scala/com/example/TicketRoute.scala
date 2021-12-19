package org

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{get, path, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{PathDirectives, RouteDirectives}
import akka.pattern.Patterns
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.example.db.data.User
import com.example.db.data.Ticket
import com.example.domain.TicketQuery
import com.example.user.actor._
import com.example.utils.{JsonUtils, TimeUtils}
import spray.json.enrichAny

import scala.concurrent.Await


class TicketRouteConfig(implicit val system: ActorSystem) extends JsonUtils {
  val ticketActor: ActorRef = system.actorOf(Props(new TicketActor()))

  implicit val mat: ActorMaterializer = ActorMaterializer()


  val getRoute: Route =

    PathDirectives.pathPrefix("tickets") {
      concat(
        path("getTickets") {
          post {
            entity(as[TicketQuery]) { query =>
              val future = Patterns.ask(ticketActor, SEARCH(query), TimeUtils.timeoutMills)
              val resultSource =Await.result(future, TimeUtils.atMostDuration).asInstanceOf[Source[Ticket,NotUsed]]
              val resultByteString = resultSource.map { it => ByteString.apply(it.toJson.toString.getBytes()) }
              RouteDirectives.complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, resultByteString))
            }
          }
        }
      )
    }
}