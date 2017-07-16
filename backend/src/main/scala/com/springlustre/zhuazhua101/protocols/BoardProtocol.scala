package com.springlustre.zhuazhua101.protocols

/**
  * Created by springlustre on 2017/5/9.
  */
trait BoardProtocol {

  case class BoardSubInfo(
    id:Long,
    name:String,
    url:String,
    title:String,
    parentBoard:String,
    boardType:String,  //section目录  board板块
    head:String = "",
    favourite:Int = 0 //0是未收藏 1是收藏
  )

  case class BoardInfo(
    id:Long,
    name:String,
    url:String,
    title:String,
    parentBoard:String,
    boardType:String,  //section目录  board板块
    sub:Seq[BoardSubInfo] = Nil,
    head:String = "",
    favourite:Int = 0 //0是未收藏 1是收藏
  )



  case class BoardListRsp(
    data:Seq[BoardInfo],
    errCode:Int = 0,
    msg:String = "ok"
  )

  case class BoardSearchRsp(
    data:Seq[BoardSubInfo],
    errCode:Int = 0,
    msg:String = "ok"
  )

}
