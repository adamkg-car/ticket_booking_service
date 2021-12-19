package com.example.user.actor

import akka.actor.{Actor, ActorLogging}
import com.example.domain.TicketQuery


import com.example.service.TicketService


class TicketActor extends Actor with ActorLogging {
  private val ticketService: TicketService = new TicketService()

  override def receive: Receive = {

    case SEARCH(ticketQuery: TicketQuery) =>
      log.info(s"received message Save with ticketQuery $ticketQuery")

      sender() ! ticketService.findTickets(ticketQuery)

    case _ =>
      log.debug("Unhandled message!")
  }
}

sealed trait TicketActorMessage


case class SEARCH(ticketQuery: TicketQuery) extends TicketActorMessage