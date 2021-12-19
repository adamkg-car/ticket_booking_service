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
import com.example.domain.LoginRequest
import com.example.domain.RegisterRequest
import com.example.domain.UserRegistrationResponse
import com.example.user.actor._
import com.example.utils.{JsonUtils, TimeUtils}
import spray.json.enrichAny

import scala.concurrent.Await


class UserRouteConfig(implicit val system: ActorSystem) extends JsonUtils {
  val userActor: ActorRef = system.actorOf(Props(new UserActor()))

  implicit val mat: ActorMaterializer = ActorMaterializer()


  val getRoute: Route =

    PathDirectives.pathPrefix("user") {
      concat(
        path("register") {
          post {
            entity(as[RegisterRequest]) { user =>
              val future = Patterns.ask(userActor, SAVE(user), TimeUtils.timeoutMills)
              val resultSource =Await.result(future, TimeUtils.atMostDuration).asInstanceOf[UserRegistrationResponse]
              val resultByteString = resultSource.toJson.toString.getBytes()
              RouteDirectives.complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, resultByteString))
            }
          }
        },

        path("login") {
          post {
            entity(as[LoginRequest]) { user =>
              val future = Patterns.ask(userActor, FIND_USER(user), TimeUtils.timeoutMills)
              val resultSource = Await.result(future, TimeUtils.atMostDuration).asInstanceOf[Source[User,NotUsed]]
              val resultByteString = resultSource.map { it => ByteString.apply(it.toJson.toString.getBytes()) }
              RouteDirectives.complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, resultByteString))
            }
          }
        }
      )
    }
}