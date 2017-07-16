package com.springlustre.zhuazhua101.utils

import java.io._

import com.springlustre.zhuazhua101.protocols._
import com.springlustre.zhuazhua101.utils.HttpUtil.Imports.getRequest
import com.springlustre.zhuazhua101.utils.http.{HttpClientUtil, InputStreamUtils}
import org.apache.http.{HttpEntity, HttpHost}
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.core.vcard.VCard
import net.glxn.qrgen.javase.QRCode
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.cookie.Cookie
import org.slf4j.LoggerFactory
import sun.misc.BASE64Encoder

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
//import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.{ClientProtocolException, CookieStore}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient}
import org.apache.http.impl.nio.client.{CloseableHttpAsyncClient, HttpAsyncClients}
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup

import scala.concurrent.ExecutionContext.Implicits.global
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
/**
  * Created by springlustre on 2017/3/29.
  */

object test {

  private val log = LoggerFactory.getLogger(this.getClass)

  def getBoard(baseUrl:String,url:String,userClient:CloseableHttpClient):Unit = {
    val sub = url.replace(baseUrl,"").drop(1)
    val parent = if(sub.contains("/")) sub.split("/").last else "none"
    val httpget = new HttpGet(url)
    //    httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    //    httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch")
    //    httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8")
    //    httpget.setHeader("Connection", "keep-alive")
    //    httpget.setHeader("Host", "www.newsmth.net")
        httpget.setHeader("Referer", "http://m.newsmth.net/section/5")
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
        httpget.setHeader("Upgrade-Insecure-Requests", 1.toString)
    try {
      val response = userClient.execute(httpget)
      val httpContent = EntityUtils.toString(InputStreamUtils.getRealEntity(response.getEntity),
        "utf-8")
      val httpCode = response.getStatusLine.getStatusCode
      EntityUtils.consume(response.getEntity)
      response.close()
      if (httpCode == HttpStatus.SC_OK && httpContent != null && httpContent.trim.length > 0) {
        val doc = Jsoup.parse(httpContent)
        println(doc)
        doc.select("#m_main").get(0).select("ul").get(0).select("li").forEach{li=>
          if(!li.getElementsByTag("font").isEmpty){
            val a = li.select("a").get(0)
            val href = a.attr("href")
            val name = href.split("/").last
            val title = a.text()
            val sec = s"sec-$name"
//            BoardDAO.insertBoard(name,href,title,sec,parent).map{res=>
//              if(res<1l){
//                println("插入失败")
//              }
//            }
            println(name+title)
            getBoard(baseUrl,baseUrl+href,userClient)
          }else{
            val a = li.select("a").get(0)
            val href = a.attr("href")
            val name = href.split("/").last
            val title = a.text()
            println(name+title)
//            BoardDAO.insertBoard(name,href,title,"",parent).map{res=>
//              if(res<1l){
//                println("插入失败")
//              }
//            }
          }
        }

      } else {
        println(httpCode)
      }
    }catch{
      case e:Exception =>
        println("e:"+e)
        println("url"+url)
    }
  }

  //获取图片
  def fetchImg(imageUrl:String) = {
    val cookieStore:CookieStore = new BasicCookieStore()
    val userClient = HttpClientUtil.getHttpClient(cookieStore,1)
    val httpget = new HttpGet(imageUrl)
//    httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//    httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch")
//    httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8")
//    httpget.setHeader("Connection", "keep-alive")
//    httpget.setHeader("Host", "www.newsmth.net")
//    httpget.setHeader("Referer", "http://www.newsmth.net/nForum/")
//    httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//    httpget.setHeader("Upgrade-Insecure-Requests", 1.toString)

    val response = userClient.execute(httpget)

    try {
      val entity = response.getEntity

      if (entity != null) {
        val httpContent = EntityUtils.toByteArray(entity)
        //图片存入磁盘
//        val fos = new FileOutputStream("E:/aaa.jpg")
//        fos.write(httpContent)
//        fos.close()
        println("data:image/jpeg;base64,"+Base64.encodeBase64String(httpContent)) // 返回Base64编码过的字节数组字符串

      }
    } finally {
      response.close()
    }

  }




  def getRequestWithCookie(url: String) = {
    val get = new HttpGet(url)
    get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    get.setHeader("Accept-Encoding", "gzip, deflate, sdch")
    get.setHeader("Accept-Language", "zh-CN,zh;q=0.8")
    get.setHeader("Cache-Control","no-cache")
    get.setHeader("Connection", "keep-alive")
    get.setHeader("Host", "m.newsmth.net")
    get.setHeader("Pragma","no-cache")
    get.setHeader("Upgrade-Insecure-Requests","1")
    get.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
//    val httpHost = new HttpHost("180.183.131.47", 8080)
    val config = RequestConfig.custom
//      .setProxy(httpHost)
      .setConnectTimeout(10000)
      .setSocketTimeout(10000)
      .setRedirectsEnabled(false)
      .setCircularRedirectsAllowed(false)
      .build
    get.setConfig(config)
    val userClient = HttpClientUtil.getHttpClient(null,1000)
    val rsp = userClient.execute(get)
    val httpContent = EntityUtils.toString(InputStreamUtils.getRealEntity(rsp.getEntity),
      "utf-8")
    val httpCode = rsp.getStatusLine.getStatusCode
    EntityUtils.consume(rsp.getEntity)
    rsp.close()
    (httpCode, httpContent)
  }

  def parseUrl = {
    val url = "http://m.newsmth.net/article/Divorce/single/937831/0"
    getRequest(url) match {
      case Right(httpContent) =>
        val doc = Jsoup.parse(httpContent)

        val nav = doc.select(".sec").select(".nav").get(0)
        val topicId = nav.select("a").get(1).attr("href").split("/").last
        val postId = nav.select("a").get(0).attr("href").split("=").last
        val quoteId = nav.select("a").get(3).attr("href").split("/").last

        val ul = doc.select(".list").select(".sec").get(0)
        val title = ul.select("li").get(0).text().drop(3)
        val mainLi = ul.select("li").get(1)
        val author = mainLi.select(".nav").get(0).select("div").get(0).select("a")
        val authorId = author.get(0).text()
        val postTime = author.get(1).text()
        val main = mainLi.select(".sp").get(0)
        val text = main.text()
        val hasQuote = text.contains("【")
        val quoteTitle = if(hasQuote) text.split("【")(1).split("】").head.replace(" ","") else ""
        val quote = if(hasQuote){
          val q = text.split("】")(1).split("--").head
          if(q.startsWith(" : ")) q.drop(3) else q
        }else ""
        val reply = (if(hasQuote){
          if(text.startsWith("【"))
            quote.split(" ").last
          else
            text.split("【").head
        }else text.split("--").head).replace(" ","<br>")
        val ip = text.split("--").last.split(" ").last

        println(topicId +" "+ postId +" "+quoteId)
        println(quoteTitle)
        println(authorId)
        println(reply)
        println(title)
        println(quote)
        println(ip)



      case Left(errCode) =>
        println(s"getRequestSend error,errCode:$errCode")

    }
  }

  def handleRecord(postId:Long) = {
    val record = mutable.HashMap(100l->90l, 80l->60l,40l->30l)
    val keys = record.toList
    var nextId = postId - 1l
    for( i <- 0 to keys.size-1){
      val a = keys(i)
      if(nextId<=a._1 && nextId>=a._2){
        nextId = a._2 - 1l
      }
    }
    nextId
  }

  def crawlHistory(now:Long) = {
    import com.github.nscala_time.time.Imports._
    val begin =  DateTime.now.hour(0).minute(0).second(0).getMillis
    val end = DateTime.now.hour(8).minute(0).second(0).getMillis
    val begin2 = DateTime.now.hour(22).minute(0).second(0).getMillis
    (now>begin && now<end) || (now>begin2)
  }

  def main(args: Array[String]): Unit = {

//    fetchAndParse("http://m.newsmth.net/article/Buddha/single/121520")

//    val now = System.currentTimeMillis()
//    import com.github.nscala_time.time.Imports._
//    val begin =  DateTime.now.hour(3).minute(5).second(0).getMillis
//    println(crawlHistory(begin))

    //判断文件是否存在
//    var a = List("")
//    Future {
//      val in = new FileInputStream(s"seed/seed.txt")
//      val inReader = new InputStreamReader(in, "UTF-8")
//      val bufferedReader = new BufferedReader(inReader)
//      bufferedReader.lines().forEach { x =>
//        a = x :: a
//      }
//    }.onComplete{
//      case Success(_) =>
//        Future {
//          val out = new FileOutputStream(s"seed/seed.txt")
//          val outWriter = new OutputStreamWriter(out, "UTF-8")
//          val bufWrite = new BufferedWriter(outWriter)
//          var write = ""
//          a.foreach { t =>
//            write = write + t +"\r\n"
//          }
//          bufWrite.write(write)
//          bufWrite.close()
//          out.close()
//        }.onComplete{
//          case Success(_) =>
//            println("3"+a)
//          case Failure(x) =>
//            println(s"failed...$x")
//        }
//        println("2"+a)
//      case Failure(x) =>
//        println(s"failed...$x")
//    }
//    println("a==="+a)

    val a ="计费hi符合dfhu"

    val x = a.lastIndexOf("(")


//    println("1"+a.substring(x).drop(1)dropRight(1))
    println("2"+a.substring(0,x))

    Thread.sleep(5000)
    }


}