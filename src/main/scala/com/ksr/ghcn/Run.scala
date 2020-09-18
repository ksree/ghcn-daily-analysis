package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.GHCN_D_RAW
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object Run {
  def main(args: Array[String]): Unit = {
    implicit val appConf: AppConfig = AppConfig.apply()
    implicit val  spark = SparkSession
      .builder()
      .appName("GHCN-DAILY-ANALYSIS")
      .config("spark.master", "local")
      .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
      .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate();
  }

  def getYearlyData(year: Int)(implicit spark: SparkSession, appConf: AppConfig): Dataset[GHCN_D_RAW] = {
    import spark.implicits._
    val data =  spark.read.format("csv")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(s"${appConf.awsBucket}/$year.csv").as[GHCN_D_RAW]
    data
  }


}
