package com.ksr.ghcn

import com.ksr.ghcn.Run.getYearlyData
import com.ksr.ghcn.conf.AppConfig
import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

class RunTest extends FlatSpec with BeforeAndAfterAll{

  implicit val appConf: AppConfig = AppConfig.apply()
  val  spark = SparkSession
    .builder()
    .appName("SomeAppName")
    .config("spark.master", "local")
    .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
    .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .getOrCreate();
  implicit val sc: SparkContext =  spark.sparkContext
  val hc: Configuration = sc.hadoopConfiguration
  hc.set("fs.s3n.awsAccessKeyId", appConf.awsKey)
  hc.set("fs.s3n.awsSecretAccessKey", appConf.awsSecret)
  hc.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

  "getYearlyData" should "return the yearly data " in {
    val data: RDD[String] = getYearlyData(1973)
    assert(data.collect().size == 100)
  }

}
