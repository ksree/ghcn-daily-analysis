package com.ksr.ghcn.domain

case class Station(id: String, latitude: Float, longitude: Float, elevation: Float, state: String, name: String, gsnFlag: String, hcnCrnFlag: String, wmoID: Int)

object Station {
  def apply(in: String): Station =
    Station(id = in.slice(0, 11),
      latitude = in.slice(12, 9).trim.toFloat,
      longitude = in.slice(22, 9).trim.toFloat,
      elevation = in.slice(31, 6).trim.toFloat,
      state = in.slice(38, 2),
      name =  in.slice(41, 30),
      gsnFlag = in.slice(72, 3),
      hcnCrnFlag = in.slice(76, 3),
      wmoID = in.slice(80, 5).trim.toInt)
}