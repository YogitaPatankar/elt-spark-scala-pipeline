name := "TelecomChurnETL"
version := "0.1"
scalaVersion := "2.12.17"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.4.1",
  "org.apache.spark" %% "spark-sql" % "3.4.1",
  "ru.yandex.clickhouse" % "clickhouse-jdbc" % "0.3.2",
  "org.postgresql" % "postgresql" % "42.2.18"
)