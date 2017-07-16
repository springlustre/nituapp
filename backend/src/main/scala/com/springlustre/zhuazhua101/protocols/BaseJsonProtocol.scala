package com.springlustre.zhuazhua101.protocols


/**
  * Created by hong on 2017/1/5.
  */
trait BaseJsonProtocol {

  sealed trait RestResponse {
    val errCode: Int
    val msg: String
  }

  case class SuccessRsp(
                         msg: String = "ok",
                         errCode: Int = 0
                       )

  case class ErrorRsp(errCode: Int, msg: String) extends RestResponse

}
