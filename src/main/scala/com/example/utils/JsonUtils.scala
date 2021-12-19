package com.example.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.example.db.data.User
import com.example.db.data.Ticket
import com.example.domain.LoginRequest
import com.example.domain.RegisterRequest
import com.example.domain.TicketQuery
import com.example.domain.UserRegistrationResponse
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}
trait JsonUtils extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object dateFormatter extends JsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = {
      JsString(obj.toString)
    }

    override def read(json: JsValue): LocalDate = {
      LocalDate.parse(json.toString(), DateTimeFormatter.ISO_DATE)
    }
  }

  implicit object integerFormatter extends JsonFormat[Integer] {
    override def write(obj: Integer): JsValue = {
      JsString(String.valueOf(obj))
    }

    override def read(json: JsValue): Integer = {
      Integer.parseInt(json.toString())
    }
  }
  implicit val userJsonFormatter: RootJsonFormat[User] = DefaultJsonProtocol.jsonFormat4(User)
  implicit val loginRequestFormatter: RootJsonFormat[LoginRequest] = DefaultJsonProtocol.jsonFormat2(LoginRequest)
  implicit val registerRequestFormat: RootJsonFormat[RegisterRequest] = jsonFormat3(RegisterRequest)
  implicit val ticketQueryFormat: RootJsonFormat[TicketQuery] = jsonFormat3(TicketQuery)
  implicit val userRegistrationRequestFormat: RootJsonFormat[UserRegistrationResponse] = jsonFormat3(UserRegistrationResponse)
  implicit val ticketResponseFormat: RootJsonFormat[Ticket] = jsonFormat9(Ticket)
}