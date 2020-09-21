package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW}
import com.ksr.ghcn.transformer.ghcnDTransform
import org.apache.spark.sql.{Dataset, SparkSession}

object Run {
  def main(args: Array[String]): Unit = {
    implicit val appConf: AppConfig = AppConfig.apply()
    implicit val spark = SparkSession
      .builder()
      .appName("GHCN-DAILY-ANALYSIS")
      .config("spark.master", "local")
      .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
      .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate();
  }

  def getYearlyRawData(year: Int)(implicit spark: SparkSession, appConf: AppConfig): Dataset[GHCN_D_RAW] = {
    import spark.implicits._
    val data = spark.read.format("csv")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(s"${appConf.awsBucket}/$year.csv")
      .toDF("id", "date", "element", "elementValue", "mFlag", "qFlag", "sFlag", "obsTime").as[GHCN_D_RAW]
    data
  }

  def transformGHCND(in: Dataset[GHCN_D_RAW])(implicit spark: SparkSession, appConf: AppConfig): Dataset[GHCN_D] = {
    import spark.implicits._
    in.map(e => ghcnDTransform.transformGHCNDRaw(e, appConf))
  }


}
