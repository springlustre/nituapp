package com.springlustre.zhuazhua101.utils

import org.apache.commons.codec.digest.DigestUtils

import scala.util.Random

/**
  * User: Taoz
  * Date: 7/8/2015
  * Time: 8:42 PM
  */
object SecureUtil {

  val random = new Random(System.currentTimeMillis())

  val chars = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  )



  def getSecurePassword(password: String, ip: String, timestamp: Long): String = {
    DigestUtils.sha1Hex(DigestUtils.md5Hex(timestamp + password) + ip + timestamp)
  }

  def checkSignature(parameters: List[String], signature: String, secureKey: String) = {
    generateSignature(parameters, secureKey) == signature
  }

  def generateSignature(parameters: List[String], secureKey: String) = {
    val strSeq = ( secureKey :: parameters ).sorted.mkString("")
    //println(s"strSeq: $strSeq")
    DigestUtils.sha1Hex(strSeq)
  }

  def generateSignatureParameters(parameters: List[String], secureKey: String) = {
    val timestamp = System.currentTimeMillis().toString
    val nonce = nonceStr(6)
    val pList = nonce :: timestamp :: parameters
    val signature = generateSignature(pList, secureKey)
    (timestamp, nonce, signature)
  }

  def nonceStr(length: Int) = {
    val range = chars.length
    (0 until length).map { _ =>
      chars(random.nextInt(range))
    }.mkString("")
  }


  def checkStringSign(str: String, sign: String, secureKey: String) = {
    stringSign(str, secureKey) == sign
  }

  def stringSign(str: String, secureKey: String) = {
    DigestUtils.sha1Hex(secureKey + str)
  }


  def main(args: Array[String]) {
    //    val appId = "test"
    //    val secureKey = "adfadfasdfa24r32452345"
    //    val sn = System.nanoTime() + ""
    //    val timestamp = System.currentTimeMillis().toString
    //    val nonce = nonceStr(6)
    //    val accountId = "accountId"
    //    val accountType = "WEIXIN"
    //    val dataVolume = 30000l
    //    val eventId = "eventId"
    //    val eventDsc = "eventDsc"
    //    val companyId = "companyId"
    //    val companyName = "companyName"
    //    val endDate = "20170604"
    //    val tradeNo = "tradeNo"
    //    val signature = generateSignature(
    //      List(appId, sn, timestamp, nonce, accountId, accountType, dataVolume.toString,
    //        eventId, eventDsc, companyId, companyName, endDate, tradeNo), secureKey
    //    )
//

    val b = DigestUtils.sha1Hex("admin@admin"+"admin@admin")
    println(b)

  }


}

