package com.springlustre.zhuazhua101.http

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.headers.CacheDirectives.{`max-age`, public}
import akka.http.scaladsl.model.headers.`Cache-Control`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import org.slf4j.Logger

import scala.concurrent.ExecutionContextExecutor

/**
  * User: Taoz
  * Date: 11/16/2016
  * Time: 10:37 PM
  *
  * 12/09/2016:   add response compress. by zhangtao
  * 12/09/2016:   add cache support self. by zhangtao
  *
  */
trait ResourceService {

  private def resources(base: String) = {
    pathPrefix("html") {
      extractUnmatchedPath { path =>
        getFromResourceDirectory(base + "/html")
      }
    } ~ pathPrefix("css") {
      extractUnmatchedPath { path =>
        getFromResourceDirectory(base + "/css")
      }
    } ~
      pathPrefix("js") {
        extractUnmatchedPath { path =>
          getFromResourceDirectory(base + "/js")
        }
      } ~
      pathPrefix("sjsout") {
        extractUnmatchedPath { path =>
          getFromResourceDirectory(base + "/sjsout")
        }
      } ~
      pathPrefix("img") {
        getFromResourceDirectory(base + "/img")
      }
  }

  //cache code copied from zhaorui.
//  private val cacheSeconds = 24 * 60 * 60
private val cacheSeconds = 0

  val resourceRoutes: Route = (pathPrefix("static") & get) {

    mapResponseHeaders { headers => `Cache-Control`(`public`, `max-age`(cacheSeconds)) +: headers } {
      encodeResponse {
        pathPrefix("pc") {
          resources("pc")
        }
      }
    }
  }


}
