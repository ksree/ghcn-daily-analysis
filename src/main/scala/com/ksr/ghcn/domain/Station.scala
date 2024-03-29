package com.ksr.ghcn.domain

case class Station(id: String, latitude: Double, longitude: Double, elevation: Double, state: String, name: String, gsnFlag: String, hcnCrnFlag: String, wmoID: Int)

object Station {
  def apply(value: String): Station = {
    val lat = value.slice(12, 20).trim match {
      case x if x.nonEmpty => x.toDouble
      case _ => 0f
    }
    val long = value.slice(22, 30).trim match {
      case x if x.nonEmpty => x.toDouble
      case _ => 0f
    }
    val ele = value.slice(31, 38).trim match {
      case x if x.nonEmpty => x.toDouble
      case _ => 0f
    }
    val wmoID = value.slice(80, 86).trim match {
      case x if x.nonEmpty => x.toInt
      case _ => 0
    }

    Station(id = value.slice(0, 11),
      latitude = lat,
      longitude = long,
      elevation = ele,
      state = value.slice(38, 40),
      name = value.slice(41, 70).trim,
      gsnFlag = value.slice(72, 75).trim,
      hcnCrnFlag = value.slice(76, 79).trim,
      wmoID = wmoID)
  }
}