organization in ThisBuild := "com.payintech"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

// Disable embedded Cassandra server
lagomCassandraEnabled in ThisBuild := false

lazy val `lagom-test` = (project in file("."))
  .aggregate(`account-api`, `account-impl`)

lazy val `account-api` = (project in file("account-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi
    )
  )

lazy val `account-impl` = (project in file("account-impl"))
  .enablePlugins(LagomJava)
  .settings(
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomJavadslServer,
      lagomJavadslTestKit,
    )
  )
  .dependsOn(`account-api`)

