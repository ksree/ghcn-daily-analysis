package com.ksr.ghcn

import com.ksr.ghcn.Run.getYearlyData
import com.ksr.ghcn.conf.AppConfig
import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{RelationalGroupedDataset, SparkSession}
import org.apache.spark.SparkContext
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

class RunTest extends FlatSpec with BeforeAndAfterAll{

  implicit val appConf: AppConfig = AppConfig.apply()
  implicit val  spark = SparkSession
    .builder()
    .appName("GHCN-DAILY-ANALYSIS")
    .config("spark.master", "local")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .getOrCreate();

  "getYearlyData" should "return the yearly data " in {
    val data = getYearlyData(1788)
    data.show(5)
    assert(data.collect().size == 1464)
  }

}
