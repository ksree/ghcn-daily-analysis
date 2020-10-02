package com.ksr.ghcn.transformer

import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalatest.FlatSpec

class ghcnDTransformTest extends FlatSpec {

  "getDate" should "convert string to date type" in {
    assert(ghcnDTransform.getDate("17880101") === java.sql.Date.valueOf("1788-01-01"))
  }

  "getPartitionDate" should "get the first day of year" in {
      val fmtr = DateTimeFormatter.ofPattern("yyyy")
    val year = ghcnDTransform.getDate("17880101").toLocalDate.getYear
      Date.valueOf(LocalDate.parse(ghcnDTransform.getDate("17880101").toLocalDate.getYear.toString, fmtr))
  }
}
