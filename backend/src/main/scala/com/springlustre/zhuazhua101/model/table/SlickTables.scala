package com.springlustre.zhuazhua101.model.table
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tOfterf.schema ++ tWxUser.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tOfterf
   *  @param uarxuc Database column uarxuc SqlType(text), Default(None) */
  final case class rOfterf(uarxuc: Option[String] = None)
  /** GetResult implicit for fetching rOfterf objects using plain SQL queries */
  implicit def GetResultrOfterf(implicit e0: GR[Option[String]]): GR[rOfterf] = GR{
    prs => import prs._
    rOfterf(<<?[String])
  }
  /** Table description of table ofterf. Objects of this class serve as prototypes for rows in queries. */
  class tOfterf(_tableTag: Tag) extends profile.api.Table[rOfterf](_tableTag, "ofterf") {
    def * = uarxuc <> (rOfterf, rOfterf.unapply)

    /** Database column uarxuc SqlType(text), Default(None) */
    val uarxuc: Rep[Option[String]] = column[Option[String]]("uarxuc", O.Default(None))
  }
  /** Collection-like TableQuery object for table tOfterf */
  lazy val tOfterf = new TableQuery(tag => new tOfterf(tag))

  /** Entity class storing rows of table tWxUser
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param openId Database column open_id SqlType(varchar), Length(100,true), Default()
   *  @param userName Database column user_name SqlType(varchar), Length(150,true), Default()
   *  @param createTime Database column create_time SqlType(int8), Default(0)
   *  @param nickName Database column nick_name SqlType(varchar), Length(150,true), Default()
   *  @param headImg Database column head_img SqlType(varchar), Length(500,true), Default()
   *  @param city Database column city SqlType(varchar), Length(50,true), Default()
   *  @param gender Database column gender SqlType(int4), Default(0)
   *  @param mobile Database column mobile SqlType(varchar), Length(20,true), Default()
   *  @param password Database column password SqlType(varchar), Length(150,true), Default()
   *  @param userType Database column user_type SqlType(int4), Default(0)
   *  @param sessionKey Database column session_key SqlType(varchar), Length(200,true), Default() */
  final case class rWxUser(id: Long, openId: String = "", userName: String = "", createTime: Long = 0L, nickName: String = "", headImg: String = "", city: String = "", gender: Int = 0, mobile: String = "", password: String = "", userType: Int = 0, sessionKey: String = "")
  /** GetResult implicit for fetching rWxUser objects using plain SQL queries */
  implicit def GetResultrWxUser(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rWxUser] = GR{
    prs => import prs._
    rWxUser.tupled((<<[Long], <<[String], <<[String], <<[Long], <<[String], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table wx_user. Objects of this class serve as prototypes for rows in queries. */
  class tWxUser(_tableTag: Tag) extends profile.api.Table[rWxUser](_tableTag, "wx_user") {
    def * = (id, openId, userName, createTime, nickName, headImg, city, gender, mobile, password, userType, sessionKey) <> (rWxUser.tupled, rWxUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(openId), Rep.Some(userName), Rep.Some(createTime), Rep.Some(nickName), Rep.Some(headImg), Rep.Some(city), Rep.Some(gender), Rep.Some(mobile), Rep.Some(password), Rep.Some(userType), Rep.Some(sessionKey)).shaped.<>({r=>import r._; _1.map(_=> rWxUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column open_id SqlType(varchar), Length(100,true), Default() */
    val openId: Rep[String] = column[String]("open_id", O.Length(100,varying=true), O.Default(""))
    /** Database column user_name SqlType(varchar), Length(150,true), Default() */
    val userName: Rep[String] = column[String]("user_name", O.Length(150,varying=true), O.Default(""))
    /** Database column create_time SqlType(int8), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
    /** Database column nick_name SqlType(varchar), Length(150,true), Default() */
    val nickName: Rep[String] = column[String]("nick_name", O.Length(150,varying=true), O.Default(""))
    /** Database column head_img SqlType(varchar), Length(500,true), Default() */
    val headImg: Rep[String] = column[String]("head_img", O.Length(500,varying=true), O.Default(""))
    /** Database column city SqlType(varchar), Length(50,true), Default() */
    val city: Rep[String] = column[String]("city", O.Length(50,varying=true), O.Default(""))
    /** Database column gender SqlType(int4), Default(0) */
    val gender: Rep[Int] = column[Int]("gender", O.Default(0))
    /** Database column mobile SqlType(varchar), Length(20,true), Default() */
    val mobile: Rep[String] = column[String]("mobile", O.Length(20,varying=true), O.Default(""))
    /** Database column password SqlType(varchar), Length(150,true), Default() */
    val password: Rep[String] = column[String]("password", O.Length(150,varying=true), O.Default(""))
    /** Database column user_type SqlType(int4), Default(0) */
    val userType: Rep[Int] = column[Int]("user_type", O.Default(0))
    /** Database column session_key SqlType(varchar), Length(200,true), Default() */
    val sessionKey: Rep[String] = column[String]("session_key", O.Length(200,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tWxUser */
  lazy val tWxUser = new TableQuery(tag => new tWxUser(tag))
}
