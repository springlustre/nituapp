package com.springlustre.zhuazhua101.model.dao

import com.springlustre.zhuazhua101.utils.DBUtil._
import slick.jdbc.{JdbcProfile, PostgresProfile}
import slick.jdbc.PostgresProfile.api._
import com.springlustre.zhuazhua101.model.table.SlickTables._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by dry on 2017/5/17.
  */
object UserDao {

  def isUserExist(openId: String) = db.run{
    tWxUser.filter(_.openId === openId).result.headOption
  }

  def updateKey(openId:String,key:String) = {
    db.run(tWxUser.filter(_.openId===openId).result.headOption).flatMap{u=>
      if(u.nonEmpty){
        db.run(tWxUser.filter(_.openId===openId).map(_.sessionKey).update(key))
      }else{
        val r = rWxUser(id = -1l, openId = openId, sessionKey = key)
        db.run(tWxUser.returning(tWxUser.map(_.id))+=r).mapTo[Long]
      }
    }
  }

  def addUser(openId: String, userName:String, createTime:Long, nickName: String,headImg:String,
              city:String,gender:Int,mobile:String,password:String,userType:Int,sessionKey:String) = {
    val r = rWxUser(-1l, openId, userName, createTime,nickName, headImg,city,gender,
      mobile,password,userType,sessionKey)
    db.run(tWxUser.returning(tWxUser.map(_.id))+=r).mapTo[Long]
  }

  def getUserByOpenId(openId: String) = db.run{
    tWxUser.filter(_.openId === openId).result.headOption
  }


}
