package com.example.service

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.example.db.data.User
import com.example.domain.LoginRequest
import com.example.domain.RegisterRequest
import com.example.domain.UserRegistrationResponse
import org.mongodb.scala.Completed
import org.mongodb.scala.result.DeleteResult
import com.example.user.repositories.UserRepo

import scala.concurrent.{ExecutionContextExecutor, Future}

class UserService {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def saveUserData: RegisterRequest => UserRegistrationResponse = (registerRequest: RegisterRequest) => {
    val userDoc:User = userMapperWithNewID(registerRequest)

    UserRepo.insertData(userDoc)
  }



  def findUser(loginRequest: LoginRequest): Source[User,NotUsed] = {
    Source.fromFuture(UserRepo.findUser(loginRequest)).mapConcat(identity)
  }

  private def userMapperWithNewID(registerRequest: RegisterRequest) = {
    User(username = registerRequest.username, email = registerRequest.email, password = registerRequest.password,
      _id = UUID.randomUUID.toString)
  }
}