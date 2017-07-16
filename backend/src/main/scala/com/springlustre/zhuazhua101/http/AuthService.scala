package com.springlustre.zhuazhua101.http


import java.net.URLDecoder

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RequestContext, ValidationRejection}
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import akka.pattern.ask
import com.springlustre.zhuazhua101.model.dao.UserDao
import com.sun.xml.internal.ws.encoding.soap.DeserializationException
import org.apache.commons.codec.digest.DigestUtils

import scala.util.{Failure, Success}

/**
  * Created by springlustre on 2017/5/9.
  */


trait AuthService extends BaseService {

  import io.circe._
  import io.circe.parser._
  import io.circe.generic.auto._
  import io.circe.syntax._

  private val logger = LoggerFactory.getLogger(this.getClass)

  def dealFutureResult(future: ⇒ Future[server.Route]) = onComplete(future) {
    case Success(route) =>
      route
    case Failure(x: DeserializationException) ⇒ reject(ValidationRejection(x.getMessage, Some(x)))
    case Failure(e) =>
      e.printStackTrace()
      logger.warn("internal error: {}", e.getMessage)
      complete(ErrorRsp(100000, "Internal error."))
  }

  def loggingAction: Directive[Tuple1[RequestContext]] = extractRequestContext.map { ctx =>
    logger.info(s"Access uri: ${ctx.request.uri} from ip ${ctx.request.uri.authority.host.address}.")
    ctx
  }

  case class UserSession(
                        uid: Long,
                        openId: String,
                        bbsId: Option[String],
                        tokenTime: Long
                        )

  case class UserInfo(
    uid: Long,
    openId: String
  )

  case class HeaderObj(
                        signature: String,
                        rawData: String,
                        openId: String
                      )

  def WxAuth(f:UserInfo => server.Route) = loggingAction { ctx =>
    val header = ctx.request.headers.map{ h =>
      (h.name, h.value())
    }.toMap
    val headerObj = header.getOrElse("headerobj", "")
    decode[HeaderObj](headerObj) match {
      case Right(rsp) =>
        val raw = URLDecoder.decode(rsp.rawData, "utf-8")
        dealFutureResult(
          UserDao.getUserByOpenId(rsp.openId).map{
            case Some(u) =>
              val sessionKey = u.sessionKey
              val signature2 = DigestUtils.sha1Hex(raw + sessionKey)
              if(rsp.signature==signature2){
                f(UserInfo(u.id,rsp.openId))
              }else{
                logger.info(s"签名不一致sign1=${rsp.signature},sign2=$signature2")
//                complete(ErrorRsp(10001,"签名不一致"))
                f(UserInfo(u.id,rsp.openId))
              }
            case None =>
              complete(ErrorRsp(10002,"用户不存在"))
          })

      case Left(e) =>
        complete(ErrorRsp(10003,"paras error."))
    }
  }


}


