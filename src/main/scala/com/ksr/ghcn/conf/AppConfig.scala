package com.ksr.ghcn.conf

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters.asScalaSetConverter

case class AppConfig(awsKey: String, awsSecret: String, awsBucket: String, countryCodesMap: Predef.Map[String, String])

object AppConfig{
  def apply(): AppConfig = {
    val conf = ConfigFactory.load()
    val cc = ConfigFactory.load("ghcnd-countries.properties")
    val countryCodesMap: Predef.Map[String, String] =
      cc.entrySet().asScala.map(e => e.getKey -> e.getValue.unwrapped().toString.trim).toMap

    AppConfig(conf.getString("AWS_ACCESS_KEY"), conf.getString("AWS_SECRET_KEY"), conf.getString("AWS_BUCKET"), countryCodesMap)
  }
}
