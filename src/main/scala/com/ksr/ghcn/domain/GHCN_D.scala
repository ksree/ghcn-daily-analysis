package com.ksr.ghcn.domain

import java.sql.Date

/*
GHCN-Daily is a dataset that contains daily observations over global land areas.
Read the docs for more information on the data
https://docs.opendata.aws/noaa-ghcn-pds/readme.html
 */
case class GHCN_D(station_id: String,
                  date: Date,
                  obs_time: String, //4-character time of observation in hour-minute format (i.e. 0700 =7:00 am)tMax: Float,
                  /*For performance sensitive code, prefer null over Option,
                   in order to avoid virtual method calls and boxing*/
                  max_temp: Option[Double],
                  min_temp: Option[Double],
                  mean_temp: Option[Double],
                  prcp: Option[Double],
                  snowfall: Option[Double],
                  snow_depth: Option[Double],
                  m_flag: String,
                  q_flag: String,
                  s_flag: String,
                  latitude: Double, //latitude of the station (in decimal degrees).
                  longitude: Double, // longitude of the station (in decimal degrees).
                  elevation: Double,
                  gsn_flag: String, // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
                  hcn_crn_flag: String, //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
                  wmo_id: Int, //World Meteorological Organization (WMO) number for the station.
                  station_name: String,
                  state_code: String, //U.S. postal code for the state (for U.S. and Canadian stations only).
                  state: String,
                  country_code: String,
                  country: String,
                  partition_date: Date
                 )
