package com.ksr.ghcn.transformer

import java.sql.Date
import java.text.SimpleDateFormat
import java.util

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW}


object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW, conf: AppConfig) = {
    val elements =  explodeElements(in.element, in.elementValue)
   /* GHCN_D(id = in.id,
      date = getDate(in.date),
      tMax= elements._1,
      tMin= elements._2,
      prcp= elements._3,
      snowfall= elements._4,
      snowDepth= elements._5,
      mFlag= in.mFlag,
      qFlag = in.qFlag,
      sFlag = in.sFlag,
      latitude = , //latitude of the station (in decimal degrees).
      longitude: Double, // longitude of the station (in decimal degrees).
      elevation: Double,
      gsnFlag: Option[String], // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
      hcnCrnFlag: Option[String], //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
      wmoID: String, //World Meteorological Organization (WMO) number for the station.
      stationName: String,
      stateCode: String, //U.S. postal code for the state (for U.S. and Canadian stations only).
      state: String,
      countryCode: String,
      country: String
    )*/
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
      case "TMIN" => (0f, 0f, 0f, 0f, value.toFloat)
      case "TMAX" => (0f, 0f, 0f, value.toFloat, 0f)
      case "PRCP" => (value.toFloat, 0f, 0f, 0f, 0f)
      case "SNOW " => (0f, value.toFloat, 0f, 0f, 0f)
      case "SNWD" => (0f, 0f, value.toFloat, 0f, 0f)
      case _ => (0f, 0f, 0f, 0f, 0f)
    }
  }
}
