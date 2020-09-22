package com.ksr.ghcn.domain

import java.sql.Date

/*
GHCN-Daily is a dataset that contains daily observations over global land areas.
Read the docs for more information on the data
https://docs.opendata.aws/noaa-ghcn-pds/readme.html
 */
case class GHCN_D(id: String,
                  date: Date,
                  obsTime: String, //4-character time of observation in hour-minute format (i.e. 0700 =7:00 am)tMax: Float,
                  tMax: Double,
                  tMin: Double,
                  prcp: Double,
                  snowfall: Double,
                  snowDepth: Double,
                  mFlag: String,
                  qFlag: String,
                  sFlag: String,
                  latitude: Double, //latitude of the station (in decimal degrees).
                  longitude: Double, // longitude of the station (in decimal degrees).
                  elevation: Double,
                  gsnFlag: String, // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
                  hcnCrnFlag: String, //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
                  wmoID: Int, //World Meteorological Organization (WMO) number for the station.
                  stationName: String,
                  stateCode: String, //U.S. postal code for the state (for U.S. and Canadian stations only).
                  state: String,
                  countryCode: String,
                  country: String
                 )
