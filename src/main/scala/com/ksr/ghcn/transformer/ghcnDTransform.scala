package com.ksr.ghcn.transformer

import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW, Station}
import org.apache.spark.sql.functions.{avg, first}
import org.apache.spark.sql.{DataFrame, Dataset}


object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW, conf: AppConfig) = {
    val elements = explodeElements(in.element, in.elementValue)
    val meanTemp: Option[Double] = (elements._1, elements._2) match {
      case (Some(max), Some(min)) => Some((max + min) / 2d)
      case _ => None
    }
    val station = conf.stationMap.getOrElse(in.id, Station("Invalid"))
    val country = getCountry(in.id, conf.countryCodesMap)
    val inDate: Date = getDate(in.date)
    GHCN_D(station_id = in.id,
      date = inDate,
      obs_time = in.obsTime,
      min_temp = elements._1, //Since the source temperature is tenths of degrees C
      max_temp = elements._2, //Since the source temperature is tenths of degrees C
      mean_temp = meanTemp, //mean daily temperature
      prcp = elements._3, //convert to mm
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
      country = country._2,
      partition_date = getPartitionDate(inDate)
    )
  }

  def getDate(date: String): Date = {
    val fmtr = DateTimeFormatter.ofPattern("yyyyMMdd")
    Date.valueOf(LocalDate.parse(date, fmtr))
  }

  def getPartitionDate(date: Date): Date = {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(date.toLocalDate.getYear, 1, 1)
    new java.sql.Date(calendar.getTimeInMillis)
  }

  def getCountry(id: String, countryCodes: Map[String, String]): (String, String) = {
    val code = id.take(2)
    val country = countryCodes.getOrElse(code, "InvalidCountryCode")
    (code, country)
  }

  def explodeElements(element: String, value: String): (Option[Double], Option[Double], Option[Double], Option[Double], Option[Double]) = {
    element match {
      case "TMIN" => (Some(value.toDouble / 10d), None, None, None, None)
      case "TMAX" => (None, Some(value.toDouble / 10d), None, None, None)
      case "PRCP" => (None, None, Some(value.toDouble / 10d), None, None)
      case "SNOW " => (None, None, None, Some(value.toDouble), None)
      case "SNWD" => (None, None, None, None, Some(value.toDouble))
      case _ => (None, None, None, None, None)
    }
  }

  //Group dataset by station id and date.Create one row per station & date containing all the measurements
  def aggregateGHCNDD(ghcndData: Dataset[GHCN_D]): DataFrame =
    ghcndData.groupBy("station_id", "date")
      .agg(first("latitude").as("latitude"),
        first("longitude").as("longitude"),
        first("elevation").as("elevation"),
        avg("max_temp").as("max_temp"),
        avg("min_temp").as("min_temp"),
        avg("mean_temp").as("mean_temp"),
        avg("prcp").as("prcp"),
        avg("snowfall").as("snowfall"),
        avg("snow_depth").as("snow_depth"),
        first("obs_time").as("obs_time"),
        first("m_flag").as("m_flag"),
        first("q_flag").as("q_flag"),
        first("s_flag").as("s_flag"),
        first("gsn_flag").as("gsn_flag"),
        first("hcn_crn_flag").as("hcn_crn_flag"),
        first("station_name").as("station_name"),
        first("state_code").as("state_code"),
        first("state").as("state"),
        first("country_code").as("country_code"),
        first("country").as("country"),
        first("wmo_id").as("wmo_id"),
        first("partition_date").as("partition_date"))
}
