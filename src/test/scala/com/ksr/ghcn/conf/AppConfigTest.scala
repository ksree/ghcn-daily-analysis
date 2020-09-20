package com.ksr.ghcn.conf

import com.ksr.ghcn.domain.Station
import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec


class AppConfigTest extends FlatSpec {

  val appConfig = AppConfig()
  val invalidStation: Station = Station("InvalidID", 0f, 0f, "", "", "", "", 0)
  "config" should "have the properties" in {
    assert(appConfig.awsKey == "test")
    assert(appConfig.awsSecret == "test")
    assert(appConfig.awsBucket == "s3a://noaa-ghcn-pds/csv/")
  }

  "country codes" should "be loaded " in {
    assert(appConfig.countryCodesMap.getOrElse("CA", "") === "Canada")
  }

  "stations" should "be loaded" in {
    val station: Station =appConfig.stationMap.getOrElse("US10york022", invalidStation)
    assert(station.id === "US10york022" )
    assert(station.elevation === "495.9" )
    assert(station.longitude === "41.0284" )
    assert(station.latitude === "-97.3984" )
    assert(station.state === "NE" )
    assert(station.name === "GRESHAM 0.2 E" )
    assert(station.gsnFlag === "" )
    assert(station.hcnCrnFlag === "" )
    assert(station.wmoID === "" )

  }
}
