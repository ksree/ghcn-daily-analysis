package com.ksr.ghcn.transformer

import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW, Station}


object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW, conf: AppConfig) = {
    val elements = explodeElements(in.element, in.elementValue)
    val station = conf.stationMap.getOrElse(in.id, Station("Invalid"))
    val country = getCountry(in.id, conf.countryCodesMap)
    GHCN_D(satation_id = in.id,
      date = getDate(in.date),
      obs_time = in.obsTime,
      min_temp = elements._1 / 10d, //Since the source temperature is tenths of degrees C
      max_temp = elements._2 / 10d, //Since the source temperature is tenths of degrees C
      mean_temp = (elements._1 + elements._2) / 2d, //mean daily temperature
      prcp = elements._3 / 10d, //convert to mm
      snowfall = elements._4, //mm
      snow_depth = elements._5, //mm
      m_flag = in.mFlag,
      q_flag = in.qFlag,
      s_flag = in.sFlag,
      latitude = station.latitude, //latitude of the station (in decimal degrees).
      longitude = station.longitude, // longitude of the station (in decimal degrees).
      elevation = station.elevation,
      gsn_flag = station.gsnFlag, // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
      hcn_crn_flag = station.hcnCrnFlag, //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
      wmo_id = station.wmoID, //World Meteorological Organization (WMO) number for the station.
      station_name = station.name,
      state_code = station.state, //U.S. postal code for the state (for U.S. and Canadian stations only).
      state = conf.stateCodesMap.getOrElse(station.state, ""),
      country_code = country._1,
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

  /*  //Group dataset by station id and date.Create one row per station & date containing all the measurements
    def groupGHCNDD(ghcndData: Dataset[GHCN_D]): DataFrame =
      ghcndData.groupBy("id", "date")
        .agg(first("latitude").as("latitude"),
          first("longitude").as("longitude"),
          first("elevation").as("elevation"),
          avg("tMax").as("max_temp"),
          avg("tMin").as("min_temp"),
          avg("prcp").as("prcp"),
          avg("snowfall").as("snowfall"),
          avg("snowDepth").as("snow_depth"),
          first("obsTime").as("obs_time"),
          first("mFlag").as("m_flag"),
          first("qFlag").as("q_flag"),
          first("sFlag").as("s_flag"),
          first("gsnFlag").as("gsn_flag"),
          first("hcnCrnFlag").as("hcn_crn_flag"),
          first("stationName").as("station_name"),
          first("stateCode").as("state_code"),
          first("state").as("state"),
          first("countryCode").as("country_code"),
          first("country").as("country"),
          first("wmoID").as("wmo_id"))*/

}
