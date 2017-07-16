
val projectName = "nituapp"
val projectVersion = "1.0.0"
val scalaV = "2.12.2"


val scalaXmlV = "1.0.6"
val akkaV = "2.5.1"
val akkaHttpV = "10.0.6"
val hikariCpV = "2.5.1"
val slickV = "3.2.0"
val logbackV = "1.1.7"
val nscalaTimeV = "2.14.0"
val codecV = "1.10"
val postgresJdbcV = "9.4.1208"


val scalaJsDomV = "0.9.1"
val scalatagsV = "0.6.2"
val sessionV = "0.2.7"


val circeVersion = "0.6.1"
val asyncHttpClientV = "2.0.32"

val playComponentV = "2.6.0-M5"
val playJsonForAkkaHttp = "1.7.0"

val diodeV = "1.1.0"

val jsoupVersion = "1.10.2"
val httpclientVersion = "4.5.3"
val httpasyncclientVersion = "4.1.3"
val commonIOVersion = "1.3.2"

val projectMainClass = "com.springlustre.zhuazhua101.Boot"

def commonSettings = Seq(
  version := projectVersion,
  scalaVersion := scalaV
)



lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(name := "shared")
  .settings(commonSettings: _*)


lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


// Scala-Js frontend
lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(name := "frontend")
  .settings(commonSettings: _*)
  .settings(inConfig(Compile)(
    Seq(
      fullOptJS,
      fastOptJS,
      packageJSDependencies,
      packageMinifiedJSDependencies
    ).map(f => (crossTarget in f) ~= (_ / "pc" / "sjsout"))
  ))
  .settings(skip in packageJSDependencies := false)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV withSources(),
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "me.chrons" %%% "diode" % diodeV,
      "com.lihaoyi" %%% "scalatags" % scalatagsV withSources()
    )
  ).dependsOn(sharedJs)


// Akka Http based backend
lazy val backend = (project in file("backend"))
  .settings(commonSettings: _*)
  .settings(
    Revolver.settings.settings,
    mainClass in Revolver.reStart := Some(projectMainClass)
  ).settings(name := projectName + "_backend")
  .settings(
    //pack
    // If you need to specify main classes manually, use packSettings and packMain
    packSettings,
    // [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String])
    packMain := Map("zhuazhua101" -> projectMainClass),
    packJvmOpts := Map("zhuazhua101" -> Seq("-Xmx64m", "-Xms32m")),
    packExtraClasspath := Map("zhuazhua101" -> Seq("."))
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scala-lang" % "scala-reflect" % scalaV,
      "org.scala-lang.modules" % "scala-xml_2.11" % scalaXmlV,
      "com.typesafe.akka" %% "akka-actor" % akkaV withSources() withSources(),
      "com.typesafe.akka" %% "akka-remote" % akkaV,
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
      "com.typesafe.akka" %% "akka-stream" % akkaV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
      "com.typesafe.slick" %% "slick" % slickV withSources(),
      "com.typesafe.slick" %% "slick-codegen" % slickV,
      "com.lihaoyi" %% "scalatags" % scalatagsV,
      "com.zaxxer" % "HikariCP" % hikariCpV,
      "ch.qos.logback" % "logback-classic" % logbackV withSources(),
      "com.github.nscala-time" %% "nscala-time" % nscalaTimeV,
      "commons-codec" % "commons-codec" % codecV,
      "org.postgresql" % "postgresql" % postgresJdbcV,
      "org.asynchttpclient" % "async-http-client" % asyncHttpClientV,
      "com.typesafe.play" %% "play-cache" % playComponentV,
      "net.glxn.qrgen" % "javase" % "2.0",
      "net.sf.ehcache" % "ehcache-core" % "2.6.11",
      "org.jsoup" % "jsoup" % jsoupVersion,
      "org.apache.httpcomponents" % "httpclient" % httpclientVersion withSources(),
      "org.apache.httpcomponents" % "httpasyncclient" % httpasyncclientVersion withSources(),
      "org.apache.commons" % "commons-io" % commonIOVersion withSources()
    )
  )
  .settings( // fastOptJS generate
    (resourceGenerators in Compile) += Def.task {
      val fastJsOut = (fastOptJS in Compile in frontend).value.data
      val fastJsSourceMap = fastJsOut.getParentFile / (fastJsOut.getName + ".map")
      Seq(
        fastJsOut,
        fastJsSourceMap
      )
    }.taskValue
  )
  .settings(
  (resourceGenerators in Compile) += Def.task {
    Seq(
      (packageScalaJSLauncher in Compile in frontend).value.data,
      (packageJSDependencies in Compile in frontend).value,
      (packageMinifiedJSDependencies in Compile in frontend).value
    )
  }.taskValue
)
  .settings(
    (resourceDirectories in Compile) += (crossTarget in frontend).value,
    watchSources ++= (watchSources in frontend).value
  )
  .dependsOn(sharedJvm)