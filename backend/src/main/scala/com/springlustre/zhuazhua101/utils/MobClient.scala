package com.springlustre.zhuazhua101.utils


import com.springlustre.zhuazhua101.common.AppSettings
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
/**
  * Created by wangc on 2017/7/8.
  */

object  MobClient extends HttpUtil with CirceSupport{
  private val log = LoggerFactory.getLogger(this.getClass)

  private val clientId = AppSettings.clientId
  private val clientKey = AppSettings.clientKey

  private val baseUrl = "https://a1.easemob.com/1110170706115172/nituapp"

  case class GetTokenParam(
                            grant_type:String,
                            client_id:String,
                            client_secret:String
                          )

  case class GetTokenRsp(
                          access_token:String,
                          expires_in:Long,
                          application:String
                        )

  /**
    * 获取token
    * @return
    */
  def getToken = {
    val params = GetTokenParam("client_credentials",clientId,clientKey).asJson.noSpaces
    val url = baseUrl + "/token"
    postJsonRequestSend(s"mob url $url", url, Nil, params).map {
      case Right(str) =>
        decode[GetTokenRsp](str) match {
          case Right(rsp) =>
            Some(rsp)

          case Left(e) =>
            log.error(s"post $url parse error " + e)
            None
        }
      case Left(e) =>
        log.error(s"post $url failed:" + e)
        None
    }

  }

  case class MsgData(
                    `type`:String,
                    msg:String
                    )
  case class ExtMsg(
                   userName:String,
                   userImg:String,
                   userId:Long
                   )

  case class SendMsg(
                      target_type:String,
                      target:List[String],
                      msg:MsgData,
                      from:String,
                      ext:ExtMsg
                    )

  /**
    * 发送消息
    * @param targetType
    * @param target
    * @param msgType
    * @param msg
    * @param from
    * @param key
    * @param userName
    * @param userImg
    * @param userId
    * @return
    */
  def sendMsg(targetType:String,target:List[String],msgType:String,msg:String,from:String,
              key:String,userName:String,userImg:String,userId:Long) = {
    val params = SendMsg(targetType,target,MsgData(msgType,msg),from,
      ExtMsg(userName,userImg,userId)).asJson.noSpaces
    val url = baseUrl + "/messages"
    postJsonRequestSend(s"mob url $url", url, Nil, params,header =
      Some(Map("Authorization"->("Bearer "+key)))).map {
      case Right(str) =>
        Some(str)

      case Left(e) =>
        log.error(s"post $url failed:" + e)
        None
    }
  }


  case class RegisterData(
                           username:String,
                           password:String,
                           nickname:String
                         )
  /**
    * 注册用户
    * @param userName
    * @param password
    * @param nickName
    */
  def RegisterUser(userName:String,password:String,nickName:String,key:String) = {
    val params = RegisterData(userName,password,nickName).asJson.noSpaces
    val url = baseUrl+"/users"
    postJsonRequestSend(s"mob url $url", url, Nil, params,header =
      Some(Map("Authorization"->("Bearer "+key)))).map {
      case Right(str) =>
        Some(str)

      case Left(e) =>
        log.error(s"post $url failed:" + e)
        None
    }
  }


  /**
    * 加入群组
    */
  def joinGroup(groupId:String,userName:String,key:String) = {
    val url = baseUrl + "/chatgroups/"+groupId+"/users/"+userName
    postJsonRequestSend(s"mob url $url", url, Nil, "",header =
      Some(Map("Authorization"->("Bearer "+key)))).map {
      case Right(str) =>
        Some(str)

      case Left(e) =>
        log.error(s"post $url failed:" + e)
        None
    }
  }



  def main(args: Array[String]): Unit = {
    getToken.map{x=>
      val key = x.get.access_token
//      sendMsg("users",List("wang"),"string","test","wangchunze123",key).map{a=>
//        println(a)
//      }
//      sendMsg("chatgroups",List("21157042520065"),"txt","你俩好啊","test1",key,
//      "王春泽","",1l).map{a=>
//        println(a)
//      }
      joinGroup("21157042520065","test1",key).map{a=>
        println(a)
      }
    }

  }

}
