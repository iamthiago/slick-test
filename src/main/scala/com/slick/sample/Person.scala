package com.slick.sample

import com.typesafe.config.ConfigFactory
import slick.dbio.DBIO
import slick.driver.MySQLDriver.api._
import slick.lifted.{ProvenShape, TableQuery}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Created by Thiago Pereira on 9/17/15.
 */
class Person(tag: Tag) extends Table[(Int, String)](tag, "person") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")

  override def * : ProvenShape[(Int, String)] = (id, name)
}

object InsertPerson extends App {

  val config = ConfigFactory.load()

  val persons = TableQuery[Person]

  val setup = DBIO.seq(
    persons.schema.create,
    persons += (1, "John"),
    persons += (2, "Ryan"),
    persons += (3, "Bryan")
  )

  val db = Database.forConfig(config.getString("running.db"))

  try {

    Await.result(db.run(setup), Duration.Inf)
    /*val result = Await.result(db.run(persons.result), Duration.Inf)
    result.foreach(x => println("id: " + x._1 + " - name: " + x._2))*/

    val q1: DBIO[Seq[(Int, String)]] = sql"""select * from person""".as[(Int, String)]
    val result = Await.result(db.run(q1), Duration.Inf)
    result.foreach(x => println("id: " + x._1 + " - name: " + x._2))

  } finally db.close()
}