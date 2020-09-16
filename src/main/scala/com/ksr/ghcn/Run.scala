package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import org.apache.spark.{SparkConf, SparkContext}

object Run {

  AppConfig.apply()
  val conf = new SparkConf().setAppName("GHCN-Daily-Analysis")
  val sc = new SparkContext(conf)
  val hc = sc.hadoopConfiguration
  hc.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
  hc.set("fs.s3n.awsAccessKeyId",myAccessKey)
  hc.set("fs.s3n.awsSecretAccessKey",mySecretKey)

}

Vielen Dank. Ich habe einen Termin gebucht