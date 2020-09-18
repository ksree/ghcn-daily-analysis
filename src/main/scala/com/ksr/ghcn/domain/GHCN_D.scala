package com.ksr.ghcn.domain

import java.sql.Date

/*
GHCN-Daily is a dataset that contains daily observations over global land areas.
Read the docs for more information on the data
https://docs.opendata.aws/noaa-ghcn-pds/readme.html
 */
case class GHCN_D(id: String,
                 date: Date,
                 tmax: Int,
                 tMin: Int,
                 prcp: Int,
                 snowfall: Int,
                 snowDepth: Int,
                 mFlag: Option[String],
                 qFlag: Option[String],
                 sFlag: Option[String],
                 latitude: Double, //latitude of the station (in decimal degrees).
                 longitude: Double, // longitude of the station (in decimal degrees).
                 elevation: Double,
                 gsnFlag: Option[String], // flag that indicates whether the station is part of the GCOS Surface Network (GSN).
                 hcnCrnFlag: Option[String], //flag that indicates whether the station is part of the U.S. Historical Climatology Network (HCN).
                 wmoID: String, //World Meteorological Organization (WMO) number for the station.
                 stationName: String,
                 stateCode: String,  //U.S. postal code for the state (for U.S. and Canadian stations only).
                 state: String,
                 countryCode: String,
                 country: String
                )
