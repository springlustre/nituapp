package com.springlustre.zhuazhua101.utils

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import slick.driver.PostgresDriver.api._
import com.springlustre.zhuazhua101.common.AppSettings._

/**
  * User: Taoz
  * Date: 2/9/2015
  * Time: 4:33 PM
  */
object DBUtil {
  val log = LoggerFactory.getLogger(this.getClass)

  private val dataSource = createDataSource()

  private def createDataSource() = {

    val dataSource = new org.postgresql.ds.PGSimpleDataSource()

    //val dataSource = new MysqlDataSource()

    log.info(s"connect to db: $slickUrl")
    dataSource.setUrl(slickUrl)
    dataSource.setUser(slickUser)
    dataSource.setPassword(slickPassword)

    val hikariDS = new HikariDataSource()
    hikariDS.setDataSource(dataSource)
    hikariDS.setMaximumPoolSize(slickMaximumPoolSize)
    hikariDS.setConnectionTimeout(slickConnectTimeout)
    hikariDS.setIdleTimeout(slickIdleTimeout)
    hikariDS.setMaxLifetime(slickMaxLifetime)
    hikariDS.setAutoCommit(true)
    hikariDS
  }

//  private def createDataSource(url:String,user:String ="nyx",pwd:String = "nYx2016") = {
//
//    val dataSource = new org.postgresql.ds.PGSimpleDataSource()
//
//    //val dataSource = new MysqlDataSource()
//
//    log.info(s"connect to db: $url")
//    dataSource.setUrl(url)
//    dataSource.setUser(user)
//    dataSource.setPassword(pwd)
//
//    val hikariDS = new HikariDataSource()
//    hikariDS.setDataSource(dataSource)
//    hikariDS.setMaximumPoolSize(slickMaximumPoolSize)
//    hikariDS.setConnectionTimeout(slickConnectTimeout)
//    hikariDS.setIdleTimeout(slickIdleTimeout)
//    hikariDS.setMaxLifetime(slickMaxLifetime)
//    hikariDS.setAutoCommit(true)
//    hikariDS
//  }

  val db = Database.forDataSource(dataSource, Some(slickMaximumPoolSize))

//  private val dataSource2 = createDataSource("jdbc:postgresql://182.92.149.40:5432/nyx2?useUnicode=true&characterEncoding=utf-8")
//
//
//  val nyx2Db = Database.forDataSource(dataSource2)
//
//  private val dataSource3 = createDataSource("jdbc:postgresql://182.92.149.40:5432/feeler2?useUnicode=true&characterEncoding=utf-8","feeler2","wo4fEEler2")
//
//  val feeler2Db = Database.forDataSource(dataSource3)

}