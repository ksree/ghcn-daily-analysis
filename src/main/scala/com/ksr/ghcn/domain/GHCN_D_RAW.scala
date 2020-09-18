package com.ksr.ghcn.domain

case class GHCN_D_RAW(
                       id: String, //11 character station identification code
                       date: String, //8 character date in YYYYMMDD format (e.g. 19860529 = May 29, 1986)
                       element: String, //4 character indicator of element type
                       elementValue: String, // 5 character data value for ELEMENT
                       mFlag: String, // character Measurement Flag
                       qFlag: String, // character Quality Flag
                       sFlag: String, // character Source Flag
                       obsTime: String //4-character time of observation in hour-minute format (i.e. 0700 =7:00 am)
                     )
