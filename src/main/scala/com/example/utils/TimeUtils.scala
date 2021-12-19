package com.example.utils

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object TimeUtils  {
  val atMostDuration: FiniteDuration = 10.seconds
  val timeoutMills: Long = 10 * 1000
}