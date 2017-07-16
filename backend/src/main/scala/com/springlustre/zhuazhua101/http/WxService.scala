package com.springlustre.zhuazhua101.http

import java.net.URLDecoder

import com.springlustre.zhuazhua101.utils.WxClient
import akka.http.scaladsl.server.Directives._
import com.springlustre.zhuazhua101.model.dao.UserDao
import com.springlustre.zhuazhua101.protocols.WxProtocol
import io.circe.generic.auto._
import io.circe.Error
import org.slf4j.LoggerFactory

/**
  * Created by dry on 2017/5/11.
  */
trait WxService extends AuthService with WxProtocol {

  private val log = LoggerFactory.getLogger(this.getClass)
  /**
    * 获取openId
    */
  private val codeSession = (path("codeSession") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error,WxLoginRst]]) {
      case Right(data)=>
      dealFutureResult {
        WxClient.code2session(data.code).map {
          case Right(res) =>
            UserDao.isUserExist(res.openid).map{x=>
              if(x.nonEmpty){
                UserDao.updateKey(res.openid,res.session_key)
              }else{ //用户不存在
                val now = System.currentTimeMillis()
                UserDao.addUser(res.openid, data.nickname, now, data.nickname, data.headimg,
                  data.city, data.gender,  "", res.openid, 0, res.session_key).map{x=>
                  if(x<1l)
                    log.error(s"add user failed...${res.openid + data.nickname}")
                }
              }
            }
            complete(SessionInfoRsp(res.openid))
          case Left(e) =>
            complete(ErrorRsp(120001, "Internal error,"))
        }
      }
      case Left(e) =>
        complete(ErrorRsp(120002, "parse data error"+e))
    }
  }


  val wxRoute = pathPrefix("wx"){
    codeSession
  }

}
