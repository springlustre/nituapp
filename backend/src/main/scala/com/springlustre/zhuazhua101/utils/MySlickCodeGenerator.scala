package com.springlustre.zhuazhua101.utils
import slick.codegen.SourceCodeGenerator
import slick.jdbc.{JdbcProfile, PostgresProfile}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * User: Taoz
  * Date: 7/15/2015
  * Time: 9:33 AM
  */
object MySlickCodeGenerator {


  import concurrent.ExecutionContext.Implicits.global

  val slickProfile = "slick.jdbc.PostgresProfile"
  val jdbcDriver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://139.129.25.229:5432/nituapp?characterEncoding=utf-8" // local
  val user = "nituapp"
  val password = "nituapp"
  val outputFolder = "target/gencode/genTablesPsql"
  val pkg = "com.springlustre.zhuazhua101.model.table"


  def genCustomTables(dbProfile: JdbcProfile) = {

    // fetch data model
    val profile: JdbcProfile =
      Class.forName(slickProfile + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]
    val dbFactory = profile.api.Database
    val db = dbFactory.forURL(url, driver = jdbcDriver,
      user = user, password = password, keepAliveConnection = true)

    // fetch data model
    val modelAction = dbProfile.createModel(Some(dbProfile.defaultTables))
    // you can filter specific tables here
    val modelFuture = db.run(modelAction)

    // customize code generator
    val codeGenFuture = modelFuture.map(model => new SourceCodeGenerator(model) {
      // override mapped table and class name
      override def entityName =
        dbTableName => "r" + dbTableName.toCamelCase

      override def tableName =
        dbTableName => "t" + dbTableName.toCamelCase
    })

    val codeGenerator = Await.result(codeGenFuture, Duration.Inf)
    codeGenerator.writeToFile(
      slickProfile, outputFolder, pkg, "SlickTables", "SlickTables.scala"
    )

  }


  def genDefaultTables() = {
    slick.codegen.SourceCodeGenerator.main(
      Array(slickProfile, jdbcDriver, url, outputFolder, pkg, user, password)
    )

  }


  def main(args: Array[String]) {
    //genDefaultTables()
    val dbProfile = PostgresProfile
    genCustomTables(dbProfile)
    println(s"Tables.scala generated in $outputFolder")

  }

}
