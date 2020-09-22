package com.ksr.ghcn.conf

import com.ksr.ghcn.domain.Station
import org.scalatest.FlatSpec


class AppConfigTest extends FlatSpec {

  val appConfig = AppConfig()
  val invalidStation: Station = Station("InvalidID", 0d, 0d, 0d, "", "", "", "", 0)
  "config" should "have the properties" in {
    assert(appConfig.awsKey == "AKIA2YJEKYFP5SB45X2V")
    assert(appConfig.awsBucket == "s3a://noaa-ghcn-pds/csv/")
  }

  "country codes" should "be loaded " in {
    assert(appConfig.countryCodesMap.getOrElse("CA", "") === "Canada")
  }

  "state codes" should "be loaded " in {
    assert(appConfig.stateCodesMap.getOrElse("NE", "") === "NEBRASKA")
  }

  "stations" should "be loaded" in {
    val station: Station = appConfig.stationMap.getOrElse("USW00014922", invalidStation)
    assert(station.id === "USW00014922")
    assert(station.latitude === 44.8831d)
    assert(station.longitude === -93.2289d)
    assert(station.elevation === 265.8d)
    assert(station.state === "MN")
    assert(station.name === "MINNEAPOLIS/ST PAUL AP       ")
    assert(station.gsnFlag === "GSN")
    assert(station.hcnCrnFlag === "HCN")
    assert(station.wmoID === 72658)
  }

}
