package com.example.service

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.example.db.data.Ticket
import com.example.domain.TicketQuery
import org.mongodb.scala.Completed
import org.mongodb.scala.result.DeleteResult
import com.example.user.repositories.TicketRepo

import scala.concurrent.{ExecutionContextExecutor, Future}

class TicketService {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def findTickets(ticketQuery: TicketQuery): Source[Ticket,NotUsed] = {
    Source.fromFuture(TicketRepo.findTickets(ticketQuery)).mapConcat(identity)
  }


}