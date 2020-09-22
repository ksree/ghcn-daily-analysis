package com.ksr.ghcn.transformer

import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW, Station}
import org.apache.spark.sql.{DataFrame, Dataset}


object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW, conf: AppConfig) = {
    val elements = explodeElements(in.element, in.elementValue)
    val station = conf.stationMap.getOrElse(in.id, Station("Invalid"))
    val country = getCountry(in.id, conf.countryCodesMap)
    GHCN_D(id = in.id,
      date = getDate(in.date),
      obsTime = "",
      tMin = elements._1 / 10d, //Since the source temperature is tenths of degrees C
      tMax = elements._2 / 10d, //Since the source temperature is tenths of degrees C
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
    val fmtr = DateTimeFormatter.ofPattern("yyyyMMdd")
    Date.valueOf(LocalDate.parse(date, fmtr))
  }

  def getCountry(id: String, countryCodes: Map[String, String]): (String, String) = {
    val code = id.take(2)
    val country = countryCodes.getOrElse(code, "InvalidCountryCode")
    (code, country)
  }

  def explodeElements(element: String, value: String) = {
    element match {
      case "TMIN" => (value.toDouble, 0d, 0d, 0d, 0d)
      case "TMAX" => (0d, value.toDouble, 0d, 0d, 0d)
      case "PRCP" => (0d, 0d, value.toDouble, 0d, 0d)
      case "SNOW " => (0d, 0d, 0d, value.toDouble, 0d)
      case "SNWD" => (0d, 0d, 0d, 0d, value.toDouble)
      case _ => (0d, 0d, 0d, 0d, 0d)
    }
  }

  import org.apache.spark.sql.functions._

  //Group dataset by station id and date.Create one row per station & date containing all the measurements
  def groupGHCNDD(ghcndData: Dataset[GHCN_D]): DataFrame =
    ghcndData.groupBy("id", "date")
      .agg(sum("latitude").as("latitude"),
        sum("longitude").as("longitude"),
        sum("elevation").as("elevation"),
        sum("tMax").as("tMax"),
        sum("tMin").as("tMin"),
        sum("prcp").as("prcp"),
        sum("snowfall").as("snowfall"),
        sum("snowDepth").as("snowDepth"),
        first("obsTime").as("obsTime"),
        first("mFlag").as("mFlag"),
        first("qFlag").as("qFlag"),
        first("sFlag").as("sFlag"),
        first("gsnFlag").as("gsnFlag"),
        first("hcnCrnFlag").as("hcnCrnFlag"),
        first("stationName").as("stationName"),
        first("stateCode").as("stateCode"),
        first("state").as("state"),
        first("countryCode").as("countryCode"),
        first("country").as("country"),
        first("wmoID").as("wmoID"))

}
