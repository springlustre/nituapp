package com.springlustre.zhuazhua101.utils

import com.springlustre.zhuazhua101.common.AppSettings
import org.slf4j.LoggerFactory
import io.circe.parser.decode
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/5/11.
  */
object WxClient extends HttpUtil with CirceSupport {

  private val log = LoggerFactory.getLogger("com.neo.sk.dogwood.utils.WxClient")

  private val appid = AppSettings.appId
  private val secret = AppSettings.secureKey

  def code2session(code: String) = {
    val baseUrl = "https://api.weixin.qq.com/sns/jscode2session"
    val paramters = List(
      "appid" -> appid,
      "secret" -> secret,
      "js_code" -> code,
      "grant_type" -> "authorization_code"
    )

    getRequestSend("code2session", baseUrl, paramters).map{
      case Right(value) =>
        decode[SessionInfo](value) match {
          case Right(res) =>
            Right(res)

          case Left(e) =>
            log.debug(s"decode code2session error.$e")
            Left(e)
        }

      case Left(e) =>
        log.debug(s"getRequestSend code2session error.$e")
        Left(e)
    }

  }

  case class SessionInfo(openid: String, session_key:String)


}
