package com.example.user.repositories

import com.example.db.config.DbConfig
import com.example.db.data.Ticket
import com.example.domain.TicketQuery
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result.DeleteResult
import com.example.utils.JsonUtils

import scala.concurrent.Future

object TicketRepo extends JsonUtils {
  private val ticketDoc: MongoCollection[Ticket] = DbConfig.tickets

  def createCollection(): Unit = {
    DbConfig.database.createCollection("tickets").subscribe(
      (result: Completed) => println(s"$result"),
      (e: Throwable) => println(e.getLocalizedMessage),
      () => println("complete"))
  }


  def findTickets(ticketQuery: TicketQuery): Future[Seq[Ticket]] = {
    ticketDoc.find(Filters.and(Filters.equal("source",ticketQuery.source),Filters.equal("destination",ticketQuery.destination),Filters.equal("date",ticketQuery.date))).toFuture()
  }


}