package com.example.db.data

import java.lang.annotation.Documented
import java.time.LocalDate

@Documented
case class User(_id: String, username: String, email: String,password: String)