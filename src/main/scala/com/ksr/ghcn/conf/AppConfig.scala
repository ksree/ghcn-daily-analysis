package com.ksr.ghcn.conf

import java.io.File

import com.ksr.ghcn.domain.Station
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters.asScalaSetConverter
import scala.io.Source

case class AppConfig(awsKey: String, awsSecret: String, awsBucket: String,
                     stateCodesMap: Predef.Map[String, String], countryCodesMap: Predef.Map[String, String],
                     stationMap: Map[String, Station], startYear: Int, endYear: Int)

object AppConfig {
  def apply(args: Array[String]): AppConfig = {
    val conf: Config =
      if (args.length == 0)
        ConfigFactory.load()
      else
        ConfigFactory.parseFile(new File(args(0).trim))


    val cc = ConfigFactory.load("ghcnd-countries.properties")
    val countryCodesMap: Predef.Map[String, String] =
      cc.entrySet().asScala.map(e => e.getKey -> e.getValue.unwrapped().toString.trim).toMap
    val sc = ConfigFactory.load("ghcnd-states.properties")
    val stateCodesMap: Predef.Map[String, String] = {
      sc.entrySet().asScala.map(e => e.getKey -> e.getValue.unwrapped().toString.trim).toMap
    }
    val stream = getClass.getResourceAsStream("/ghcnd-stations.properties")

    val bfr = Source.fromInputStream(stream)
    val stationMap: Map[String, Station] = (for {
      line <- bfr.getLines
      station = Station(line)
    } yield (station.id -> station)).toMap
    bfr.close()

    AppConfig(conf.getString("AWS_ACCESS_KEY"), conf.getString("AWS_SECRET_KEY"),
      conf.getString("AWS_BUCKET"), stateCodesMap, countryCodesMap, stationMap, conf.getInt("startYear"), conf.getInt("endYear"))
  }
}
