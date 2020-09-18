package com.ksr.ghcn.conf

import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec

import scala.collection.JavaConverters.asScalaSetConverter


class AppConfigTest extends FlatSpec {

  val appConfig = AppConfig()

  "config" should "have the properties" in {
    assert(appConfig.awsKey == "test")
    assert(appConfig.awsSecret == "test")
    assert(appConfig.awsBucket == "s3a://noaa-ghcn-pds/csv/")
  }

  "country codes" should "be loaded " in {
    assert(appConfig.countryCodesMap.getOrElse("CA", "") === "Canada")

  }
}
