package com.ksr.ghcn.transformer

import java.sql.Date
import java.text.SimpleDateFormat
import java.util

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW, Station}


object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW, conf: AppConfig) = {
    val elements = explodeElements(in.element, in.elementValue)
    val station = conf.stationMap.getOrElse(in.id, Station("Invalid"))
    val country = getCountry(in.id, conf.countryCodesMap)
    GHCN_D(id = in.id,
      date = getDate(in.date),
      obsTime = "",
      tMin = elements._1,
      tMax = elements._2,
      prcp = elements._3,
      snowfall = elements._4,
      snowDepth = elements._5,
      mFlag = in.mFlag,
      qFlag = in.qFlag,
      sFlag = in.sFlag,
      latitude = station.latitude, //latitude of the station (in decimal degrees).
      longitude = station.longitude, // longitude of the station (in decimal degrees).
      elevation = station.elevation,
      gsnFlag = station.gsnFlag, // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
      hcnCrnFlag = station.hcnCrnFlag, //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
      wmoID = station.wmoID, //World Meteorological Organization (WMO) number for the station.
      stationName = station.name,
      stateCode = station.state, //U.S. postal code for the state (for U.S. and Canadian stations only).
      state = conf.stateCodesMap.getOrElse(station.state, ""),
      countryCode = country._1,
      country = country._2
    )
  }

  def getDate(date: String): Date = {
    val formatter = new SimpleDateFormat("YYYYMMdd")
    val pDate: util.Date = formatter.parse(date)
    new Date(pDate.getTime())
  }

  def getCountry(id: String, countryCodes: Map[String, String]): (String, String) = {
    val code = id.take(2)
    val country = countryCodes.getOrElse(code, "InvalidCountryCode")
    (code, country)
  }

  def explodeElements(element: String, value: String) = {
    element match {
      case "TMIN" => (value.toFloat, 0f, 0f, 0f, 0f)
      case "TMAX" => (0f, value.toFloat, 0f, 0f, 0f)
      case "PRCP" => (0f, 0f, value.toFloat, 0f, 0f)
      case "SNOW " => (0f, 0f, 0f, value.toFloat, 0f)
      case "SNWD" => (0f, 0f, 0f, 0f, value.toFloat)
      case _ => (0f, 0f, 0f, 0f, 0f)
    }
  }
}
