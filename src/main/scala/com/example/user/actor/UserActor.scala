package com.example.user.actor

import akka.actor.{Actor, ActorLogging}
import com.example.domain.RegisterRequest
import com.example.domain.LoginRequest

import com.example.service.UserService


class UserActor extends Actor with ActorLogging {
  private val userService: UserService = new UserService()

  override def receive: Receive = {

    case SAVE(user: RegisterRequest) =>
      log.info(s"received message Save with employee $user")

      sender ! userService.saveUserData(user)

    case FIND_USER(loginRequest: LoginRequest) =>
      log.info(s"received message Save with employee $loginRequest")

      sender() ! userService.findUser(loginRequest)

    case _ =>
      log.debug("Unhandled message!")
  }
}

sealed trait UserActorMessage

case class SAVE(user: RegisterRequest) extends UserActorMessage

case class FIND_USER(loginRequest: LoginRequest) extends UserActorMessage