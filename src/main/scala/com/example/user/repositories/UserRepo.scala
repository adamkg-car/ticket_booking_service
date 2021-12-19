package com.example.user.repositories

import com.example.db.config.DbConfig
import com.example.db.data.User
import com.example.domain.LoginRequest
import com.example.domain.RegisterRequest
import com.example.domain.UserRegistrationResponse
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result.DeleteResult
import com.example.utils.JsonUtils
import com.example.utils.TimeUtils
import scala.concurrent.Await



import scala.concurrent.Future

object UserRepo extends JsonUtils {
  private val userDoc: MongoCollection[User] = DbConfig.users

  def createCollection(): Unit = {
    DbConfig.database.createCollection("users").subscribe(
      (result: Completed) => println(s"$result"),
      (e: Throwable) => println(e.getLocalizedMessage),
      () => println("complete"))
  }

  def insertData(user: User): UserRegistrationResponse = {
    try{
        val checkIfExists: Future[Seq[User]] = userDoc.find(Filters.equal("email",user.email)).toFuture()
        val resultSource = Await.result(checkIfExists,TimeUtils.atMostDuration).asInstanceOf[Seq[User]]
        if(resultSource.length>=1){
            UserRegistrationResponse(message="User Already Exists",username = user.username, email= user.email)
        }else{
            userDoc.insertOne(user).toFuture()
            UserRegistrationResponse(message="User Registration Succesful",username = user.username, email= user.email)
        }

    }catch{
        case exception: Exception =>{
            UserRegistrationResponse(message="Some Error Occured",username = user.username, email= user.email)
        }
    }
  }

  def findUser(loginRequest: LoginRequest): Future[Seq[User]] = {
    userDoc.find(Filters.and(Filters.equal("email",loginRequest.email),Filters.equal("password",loginRequest.password))).toFuture()
  }


}