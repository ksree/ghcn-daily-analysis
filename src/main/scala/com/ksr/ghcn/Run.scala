package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Run {
  def main(args: Array[String]): Unit = {
    val appConf = AppConfig.apply()
    val conf = new SparkConf().setAppName("GHCN-Daily-Analysis")
    implicit val sc = new SparkContext(conf)
    val hc = sc.hadoopConfiguration
    hc.set("fs.s3a.endpoint", "s3.amazonaws.com")
    hc.set("fs.s3n.awsAccessKeyId", appConf.awsKey)
    hc.set("fs.s3n.awsSecretAccessKey", appConf.awsSecret)

    val sample: RDD[String] = sc.textFile(s"${appConf.awsBucket}/1763.csv")

    println(sample.collect().size)
  }

  def getYearlyData(year: Int)(implicit sc: SparkContext, appConf: AppConfig): RDD[String] = {
    val data =  sc.textFile(s"${appConf.awsBucket}/$year.csv")
    data
  }
}
