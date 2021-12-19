package com.example.db.data

import java.lang.annotation.Documented
import java.time.LocalDate

@Documented
case class Ticket(_id: String, count: Integer, date: String, destination: String, source: String, trainName: String, trainNo: String, arrivalDate: String, departureDate: String)