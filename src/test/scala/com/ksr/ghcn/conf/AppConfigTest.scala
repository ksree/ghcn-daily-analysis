package com.ksr.ghcn.conf

import org.scalatest.FlatSpec

class AppConfigTest extends FlatSpec {

  val appConfig = AppConfig()

  "config" should "provide the app config properties" in {
    assert(appConfig.awsKey == "test")
    assert(appConfig.awsSecret == "test")
    assert(appConfig.awsBucket == "s3://noaa-ghcn-pds/csv/1763.csv")

  }

}
