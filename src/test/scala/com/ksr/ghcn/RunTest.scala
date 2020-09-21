package com.ksr.ghcn

import com.ksr.ghcn.Run.{transformGHCND, getYearlyRawData}
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

  val yearlyRawData = getYearlyRawData(1788)

  "getYearlyRawData" should "return the yearly data " in {
    yearlyRawData.show(5)
    assert(yearlyRawData.collect().size == 1464)
  }

  "transformGHCND" should "return the yearly data " in {
    val ghcndData = transformGHCND(yearlyRawData)
    ghcndData.show(5)
    assert(ghcndData.collect().size == 1464)
  }
}
