package com.springlustre.zhuazhua101.utils

import java.nio.charset.Charset
import javax.net.ssl.SSLContext

import com.springlustre.zhuazhua101.utils.http.{HttpClientUtil, InputStreamUtils}
import org.apache.http.HttpStatus
import org.apache.http.conn.ssl.SSLContexts
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.asynchttpclient.{BoundRequestBuilder, DefaultAsyncHttpClient, Param, Response}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}

/**
  * User: Taoz
  * Date: 11/28/2016
  * Time: 17:28 PM
  *
  * update by zhangtao 2017-01-01 23:54
  * 1, remove unused import "akka.stream.Materializer"
  * 2, change "implicit val executor" to implicit in the function.
  * 3, you can import HttpUtil.Imports to use the api.
  *
  *
  *
  */

object HttpUtil {

  import org.apache.http.conn.ssl.SSLConnectionSocketFactory
  import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
  import org.apache.http.impl.nio.client.HttpAsyncClients

//  val acceptingTrustStrategy = new Nothing() {
//    def isTrusted(certificate: Array[Nothing], authType: String) = true
//  }
//  var sslContext : SSLContext= _
//  try {
//    sslContext = SSLContexts.custom.loadTrustMaterial(null, acceptingTrustStrategy).build
//  } catch {
//    case e: Exception =>
//  }
//
//  val client: CloseableHttpAsyncClient = HttpAsyncClients.custom.setHostnameVerifier(
//    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).
//    setSSLContext(sslContext).build

  private val ahClientImp = new DefaultAsyncHttpClient()
  private val log = LoggerFactory.getLogger(this.getClass)

  object Imports extends HttpUtil

  implicit class AhcToScala[T](reqBuilder: BoundRequestBuilder) {

    def scalaExecute(): Future[Response] = {
      import org.asynchttpclient.AsyncCompletionHandler
      val result = Promise[Response]()
      reqBuilder.execute(new AsyncCompletionHandler[Response]() {
        override def onCompleted(response: Response): Response = {
          result.success(response)
          response
        }
        override def onThrowable(t: Throwable): Unit = {
          result.failure(t)
        }
      })
      result.future
    }
  }
}

trait HttpUtil {


  import HttpUtil._

  //implicit val executor: ExecutionContext

  private val ahClient: DefaultAsyncHttpClient = ahClientImp

  import collection.JavaConverters._

  private def parseResp(response: Response, charset: Charset) = {
    val body = new String(response.getResponseBodyAsBytes, charset)
    log.debug("getRequestSend response headers:" + response.getHeaders)
    log.debug("getRequestSend response body:" + body)
    if (response.getStatusCode != 200) {
      val uri = response.getUri
      val bodyLength = body.length
      val msg = s"getRequestSend http failed url = $uri, status = ${response.getStatusCode}, text = ${response.getStatusText}, body = ${body.substring(0, Math.min(bodyLength, 1024))}"
      log.warn(msg)
    }
    body
  }

  private def executeRequest(
                              methodName: String,
                              request: BoundRequestBuilder,
                              charset: Charset
                            )(implicit executor: ExecutionContext) = {
    request.scalaExecute().map { response =>
      Right(parseResp(response, charset))
    }.recover { case e: Throwable => Left(e) }
  }

  def postJsonRequestSend(
                           methodName: String,
                           url: String,
                           parameters: List[(String, String)],
                           jsonStr: String,
                           responseCharsetName: String = "UTF-8",
                           header:Option[Map[String,String]] = None
                         )(implicit executor: ExecutionContext): Future[Either[Throwable, String]] = {
    log.info("Post Request [" + methodName + "] Processing...")
    log.debug(methodName + " url=" + url)
    log.debug(methodName + " parameters=" + parameters)
    log.debug(methodName + " postData=" + jsonStr)
    val cs = Charset.forName(responseCharsetName)
    val request = ahClient.
      preparePost(url).
      setFollowRedirect(true).
      setRequestTimeout(20 * 1000).
      addQueryParams(parameters.map { kv => new Param(kv._1, kv._2) }.asJava).
      addHeader("Content-Type", "application/json").
      setBody(jsonStr).
      setCharset(cs)
    val r = if(header.nonEmpty){
      header.get.foreach{x=>
        request.addHeader(x._1,x._2)
      }
      request
    }else{
      request
    }
    executeRequest(methodName, r, cs)
  }

  def getRequestSend(
                      methodName: String,
                      url: String,
                      parameters: List[(String, String)],
                      responseCharsetName: String = "UTF-8"
                    )(implicit executor: ExecutionContext): Future[Either[Throwable, String]] = {
    log.info("Get Request [" + methodName + "] Processing...")
    log.debug(methodName + " url=" + url)
    log.debug(methodName + " parameters=" + parameters)
    val cs = Charset.forName(responseCharsetName)
    val request = ahClient.
      prepareGet(url).
      setFollowRedirect(true).
      setRequestTimeout(20 * 1000).
      addQueryParams(parameters.map { kv => new Param(kv._1, kv._2) }.asJava).
      setCharset(cs)
    executeRequest(methodName, request, cs)
  }

  def getRequest(
                  url: String,
                  responseCharsetName: String = "utf-8"
                )  = {
    var userClient: CloseableHttpClient = null
    userClient = HttpClientUtil.getHttpClient(null, 1)
    val response = HttpClientUtil.executeHttpGet(url, userClient, null)
    val httpContent = EntityUtils.toString(InputStreamUtils.getRealEntity(response.getEntity), responseCharsetName)
    val httpCode = response.getStatusLine.getStatusCode
    EntityUtils.consume(response.getEntity)
    response.close()
    if (httpCode == HttpStatus.SC_OK && httpContent != null && httpContent.trim.length > 0){
      Right(httpContent)
    }
    else{
      Left(httpCode)
    }
  }

}