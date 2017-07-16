package com.springlustre.zhuazhua101.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers.CacheDirectives.{`max-age`, `no-cache`, public}
import akka.http.scaladsl.server.Directives.path

import scala.concurrent.Future


/**
  * Created by springlustre on 2016/12/13.
  **/
trait HttpService
  extends ResourceService
  with WxService
{

  private val adminEnter =  pathPrefix("pc"){
    addCacheControlHeaders(`no-cache`) {
      getFromResource("pc/html/admin.html")
    }
  }


  val routes : Route = pathPrefix("nituapp") {
//    complete("hello")
    resourceRoutes ~ adminEnter ~ wxRoute
  }







}
