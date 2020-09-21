package com.ksr.ghcn.conf

import com.ksr.ghcn.domain.Station
import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec

import scala.io.Source


class AppConfigTest extends FlatSpec {

  val appConfig = AppConfig()
  val invalidStation: Station = Station("InvalidID", 0f, 0f, 0f,  "", "", "", "", 0)
  "config" should "have the properties" in {
    assert(appConfig.awsKey == "test")
    assert(appConfig.awsSecret == "test")
    assert(appConfig.awsBucket == "s3a://noaa-ghcn-pds/csv/")
  }

  "country codes" should "be loaded " in {
    assert(appConfig.countryCodesMap.getOrElse("CA", "") === "Canada")
  }

  "state codes" should "be loaded " in {
    assert(appConfig.stateCodesMap.getOrElse("NE", "") === "NEBRASKA")
  }

  "stations" should "be loaded" in {
    val station: Station =appConfig.stationMap.getOrElse("USW00014922", invalidStation)
    assert(station.id === "USW00014922" )
    assert(station.latitude === 44.8831f )
    assert(station.longitude === -93.2289f )
    assert(station.elevation === 265.8f )
    assert(station.state === "MN" )
    assert(station.name === "MINNEAPOLIS/ST PAUL AP       " )
    assert(station.gsnFlag === "GSN" )
    assert(station.hcnCrnFlag === "HCN" )
    assert(station.wmoID === 72658 )
  }
}
