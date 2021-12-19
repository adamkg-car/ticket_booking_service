package com.example.db.config

import ch.rasc.bsoncodec.math.BigDecimalStringCodec
import ch.rasc.bsoncodec.time.LocalDateTimeDateCodec
import com.mongodb.MongoCredential.createCredential
import com.mongodb.{MongoCredential, ServerAddress}
import org.bson.codecs.configuration.CodecRegistry
import com.example.db.data.User
import com.example.db.data.Ticket
import org.mongodb.scala.MongoClientSettings
import com.mongodb.ConnectionString;
import org.mongodb.scala._



import scala.collection.JavaConverters.seqAsJavaListConverter

object DbConfig {

 

  import org.bson.codecs.configuration.CodecRegistries
  import org.bson.codecs.configuration.CodecRegistries._
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._
  import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}


  private val javaCodecs = CodecRegistries.fromCodecs(
    new LocalDateTimeDateCodec(),
    new BigDecimalStringCodec())

  private val registry: CodecRegistry = CodecRegistries.fromProviders(classOf[User],classOf[Ticket])


  val uri: String = "mongodb+srv://raju:raju@cluster0.m3dou.gcp.mongodb.net/ticketing_service?retryWrites=true&w=majority"


  val client: MongoClient = MongoClient(uri)

  val database: MongoDatabase = client.getDatabase("ticketing_service").withCodecRegistry(fromRegistries(registry, javaCodecs, DEFAULT_CODEC_REGISTRY))

  val users: MongoCollection[User] = database.getCollection("user")

  val tickets: MongoCollection[Ticket] = database.getCollection("tickets")


}