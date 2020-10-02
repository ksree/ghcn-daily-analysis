package com.ksr.ghcn

import com.ksr.ghcn.Run.{readGHCNDData, transformGHCND, writeGHCND}
import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW}
import com.ksr.ghcn.transformer.ghcnDTransform.aggregateGHCNDD
import org.apache.spark.sql.{Dataset, SparkSession}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Ignore}

//Tests ignore; first set your aws key and secret in test resources application.conf, and then enable this test
@Ignore
class RunIntegrationTest extends FlatSpec with BeforeAndAfterAll {
/*
  implicit lazy val appConf: AppConfig = AppConfig.apply(Array.empty[String])
  implicit lazy val spark: SparkSession = SparkSession
    .builder()
    .appName("GHCN-DAILY-ANALYSIS")
    .config("spark.master", "local")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .getOrCreate();


  val rawData: Dataset[GHCN_D_RAW] = readGHCNDData(1788)
  val ghcndData: Dataset[GHCN_D] = transformGHCND(rawData)

  "getYearlyRawData" should "return the yearly data " in  {
    assert(rawData.collect().length == 1464)
  }

  "transformGHCND" should "return the yearly data " in  {
    assert(aggregateGHCNDD(ghcndData).collect().length == 732)
  }

  "writeGHCND" should "populate bigquery tables" in  {
    writeGHCND(ghcndData.limit(10))
  }*/

}
