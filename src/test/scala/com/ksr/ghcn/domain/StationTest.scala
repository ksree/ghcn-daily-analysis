package com.ksr.ghcn.domain

import org.scalatest.{FlatSpec, FunSuite}

class StationTest extends FlatSpec {

  "station" should "return valid values" in {
    val station = Station("ITE00100554  45.4717    9.1892  150.0    MILAN                                       ")
    assert(station.id == "ITE00100554")
    assert(station.latitude == 45.4717)
    assert(station.longitude == 9.1892)
    assert(station.elevation == 150.0)
    assert(station.name.trim == "MILAN")

  }

}
