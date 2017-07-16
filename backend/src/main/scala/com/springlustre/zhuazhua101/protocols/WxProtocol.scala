package com.springlustre.zhuazhua101.protocols

/**
  * Created by dry on 2017/5/11.
  */

trait WxProtocol extends BaseJsonProtocol{

  case class SessionInfo(openid: String, sessionKey:String)

  case class SessionInfoRsp(data: String, errCode: Int = 0, msg: String = "ok")

  case class WxUserInfo(
                       userId:Long,
                       openId:String,
                       userName:String,
                       headImg:String,
                       userType:Int
                       )

  case class WxLoginRst(
                         code:String,
                         nickname: String,
                         headimg: String,
                         city: String,
                         gender:Int
                       )

  case class UserInfoRsp(
                         data: WxUserInfo,
                         errCode: Int = 0,
                         msg: String = "ok"
                       )

}
