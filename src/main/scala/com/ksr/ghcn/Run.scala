package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import com.ksr.ghcn.domain.{GHCN_D, GHCN_D_RAW}
import com.ksr.ghcn.transformer.ghcnDTransform
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

object Run {
  def main(args: Array[String]): Unit = {
    implicit val appConf: AppConfig = AppConfig.apply(args)
    implicit val spark = SparkSession
      .builder()
      .appName("GHCN-DAILY-ANALYSIS")
      .config("spark.hadoop.fs.s3a.access.key", appConf.awsKey)
      .config("spark.hadoop.fs.s3a.secret.key", appConf.awsSecret)
      .getOrCreate();
    for(i <- appConf.startYear to appConf.endYear) {
      val rawData: Dataset[GHCN_D_RAW] = readGHCNDData(i)
      val ghcndData: Dataset[GHCN_D] = transformGHCND(rawData)
      writeGHCND(ghcndData)
    }

  }

  def readGHCNDData(year: Int)(implicit spark: SparkSession, appConf: AppConfig): Dataset[GHCN_D_RAW] = {
    import spark.implicits._
    spark.read.format("csv")
      .option("inferSchema", "true")
      .option("header", "false")
      .load(s"${appConf.awsBucket}/$year.csv")
      .toDF("id", "date", "element", "elementValue", "mFlag", "qFlag", "sFlag", "obsTime").as[GHCN_D_RAW]
  }

  def transformGHCND(in: Dataset[GHCN_D_RAW])(implicit spark: SparkSession, appConf: AppConfig): Dataset[GHCN_D] = {
    import spark.implicits._
    ghcnDTransform.aggregateGHCNDD(in.map(ghcnDTransform.transformGHCNDRaw(_, appConf)).as[GHCN_D]).as[GHCN_D]
  }

  def writeGHCND(out: Dataset[GHCN_D])(implicit spark: SparkSession, appConf: AppConfig)= {
    out.write
      .format("bigquery")
      .mode(SaveMode.Append)
      .option("temporaryGcsBucket", appConf.tempGCSBucket)
      .option("partitionField", "partition_date")
      .option("partitionType", "DAY")
      .option("clusteredFields", "country")
      .option("allowFieldAddition", "true")  //Adds the ALLOW_FIELD_ADDITION SchemaUpdateOption
      .save(appConf.bigQueryTableName)
  }
}
