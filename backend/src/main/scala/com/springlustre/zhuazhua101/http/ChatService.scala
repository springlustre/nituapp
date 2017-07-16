package com.springlustre.zhuazhua101.http

import akka.http.scaladsl.server.Directives.pathPrefix
import com.springlustre.zhuazhua101.protocols.ChatProtocol

/**
  * Created by wangc on 2017/7/8.
  */

trait ChatService extends AuthService with ChatProtocol{

  val sendMsg = ()

//  val wxRoute = pathPrefix("chat"){
//
//  }

}
