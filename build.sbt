organization in ThisBuild := "com.payintech"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

// Disable embedded Cassandra server
lagomCassandraEnabled in ThisBuild := false

// Version of java
val javaVersion = settingKey[String]("The version of Java used for building.")
javaVersion := System.getProperty("java.version")

// Project
lazy val `lagom-test` = (project in file("."))
  .aggregate(`account-api`, `account-impl`)

lazy val `account-api` = (project in file("account-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomLogback
    )
  )

lazy val `account-impl` = (project in file("account-impl"))
  .enablePlugins(LagomJava, PlayEbean)
  .settings(
    libraryDependencies ++= Seq(
      //      guice,
      lagomLogback,
      lagomJavadslServer,
      lagomJavadslTestKit,
      lagomJavadslPersistenceJdbc,
      //      lagomJavadslAkkaDiscovery,
      "org.postgresql" % "postgresql" % "42.2.5",
    ),
    libraryDependencies ++= java9AndSupLibraryDependencies
  )
  .dependsOn(`account-api`)

//lazy val `phone-api` = (project in file("phone-api"))
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomJavadslApi
//    )
//  )
//  .dependsOn(`account-api`)
//
//lazy val `phone-impl` = (project in file("phone-impl"))
//  .enablePlugins(LagomJava, PlayEbean)
//  .settings(
//    libraryDependencies ++= Seq(
//      //      guice,
//      lagomLogback,
//      lagomJavadslServer,
//      lagomJavadslTestKit,
//      lagomJavadslPersistenceJdbc,
//      //      lagomJavadslAkkaDiscovery,
//      "org.postgresql" % "postgresql" % "42.2.5",
//    ),
//    libraryDependencies ++= java9AndSupLibraryDependencies
//  )
//  .dependsOn(`account-api`)
//  .dependsOn(`phone-api`)

// Library dependencies for java 9 and later
lazy val java9AndSupLibraryDependencies: Seq[sbt.ModuleID] =
  if (!javaVersion.toString.startsWith("1.8")) {
    Seq(
      "com.sun.activation" % "javax.activation" % "1.2.0",
      "com.sun.xml.bind" % "jaxb-core" % "2.3.0",
      "com.sun.xml.bind" % "jaxb-impl" % "2.3.1",
      "javax.jws" % "javax.jws-api" % "1.1",
      "javax.xml.bind" % "jaxb-api" % "2.3.0",
      "javax.xml.ws" % "jaxws-api" % "2.3.1"
    )
  } else {
    Seq.empty
  }

// Disable the play enhancer
playEnhancerEnabled := false

lagomKafkaEnabled in ThisBuild := !sys.env.keySet.contains("DISABLE_EMBEDDED_KAFKA")
