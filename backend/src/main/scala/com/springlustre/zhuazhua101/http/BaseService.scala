package com.springlustre.zhuazhua101.http

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.headers.{CacheDirective, `Cache-Control`}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.Materializer
import akka.util.Timeout
import com.springlustre.zhuazhua101.protocols.BaseJsonProtocol
import com.springlustre.zhuazhua101.utils.CirceSupport
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by ZYQ on 2017/1/4.
  */
trait BaseService extends CirceSupport with BaseJsonProtocol{
  def addCacheControlHeaders(first: CacheDirective, more: CacheDirective*): Directive0 = {
    mapResponseHeaders { headers =>
      `Cache-Control`(first, more: _*) +: headers
    }
  }

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  implicit val timeout: Timeout


  private val log = LoggerFactory.getLogger(this.getClass)

  lazy val regex = "(10.*|localhost|192.*|127.*)".r

}
