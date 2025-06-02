import org.apache.spark.sql.{SparkSession, SaveMode}
import org.apache.spark.sql.functions._

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("TelecomChurnETL").master("local[*]").getOrCreate()
    val df = spark.read.option("header", "true").option("inferSchema", "true").csv("/data/telecom_churn.csv")
    val filled = df.na.fill(Map("TotalCharges" -> 0, "MonthlyCharges" -> 0))
    val anonymized = filled.withColumn("CustomerID", sha2(col("CustomerID").cast("string"), 256)).drop("PhoneNumber")

    anonymized.write.format("jdbc")
      .option("url", "jdbc:clickhouse://clickhouse:8123/default")
      .option("dbtable", "telecom_staging")
      .option("driver", "ru.yandex.clickhouse.ClickHouseDriver")
      .mode(SaveMode.Overwrite).save()

    anonymized.write.format("jdbc")
      .option("url", "jdbc:postgresql://postgres:5432/analytics")
      .option("dbtable", "telecom_final")
      .option("user", "user").option("password", "pass")
      .option("driver", "org.postgresql.Driver")
      .mode(SaveMode.Overwrite).save()

    spark.stop()
  }
}