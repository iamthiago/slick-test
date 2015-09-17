package com.slick.sample

import com.typesafe.config.ConfigFactory
import slick.driver.MySQLDriver.api._
import slick.lifted.{ProvenShape, TableQuery}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Created by Thiago Pereira on 9/17/15.
 */
object ProductBySupplier extends App {

  val config = ConfigFactory.load()

  class Supplier(tag: Tag) extends Table[(Int, String)](tag, "supplier") {
    def supplierId = column[Int]("supplier_id", O.PrimaryKey)
    def name = column[String]("name")

    override def * : ProvenShape[(Int, String)] = (supplierId, name)
  }

  val suppliers = TableQuery[Supplier]

  class Product(tag: Tag) extends Table[(Int, String, Int)](tag, "product") {
    def productId = column[Int]("product_id", O.PrimaryKey)
    def name = column[String]("name")
    def supplierId = column[Int]("supplier_id")

    def supplier = foreignKey("supplier_fk", supplierId, suppliers)(_.supplierId)

    override def * : ProvenShape[(Int, String, Int)] = (productId, name, supplierId)
  }

  val products = TableQuery[Product]

  val setup = DBIO.seq(
    (products.schema ++ suppliers.schema).create,

    suppliers ++= Seq(
      (1, "Walmart"),
      (2, "Amazon")
    ),

    products ++= Seq(
      (1, "DVD 1", 1),
      (2, "DVD 2", 1),
      (3, "DVD 3", 1),
      (4, "DVD 4", 1),
      (5, "DVD 5", 1),
      (6, "DVD 6", 2),
      (7, "DVD 7", 2),
      (8, "DVD 8", 2),
      (9, "DVD 9", 2),
      (10, "DVD 10", 2)
    )
  )

  val db = Database.forConfig(config.getString("running.db"))

  try {

    Await.result(db.run(setup), Duration.Inf)

    val q1 =
      for {
        s <- suppliers
        p <- products if p.supplierId === s.supplierId
      } yield (p.name, s.name)

    val result = Await.result(db.run(q1.result), Duration.Inf)
    result.foreach(x => println("product: " + x._1 + " - supplier: " + x._2))

  } finally db.close()
}