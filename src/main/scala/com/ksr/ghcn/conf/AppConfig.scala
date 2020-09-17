package com.ksr.ghcn.conf
import com.typesafe.config.ConfigFactory
case class AppConfig(awsKey: String, awsSecret: String, awsBucket: String)

object AppConfig  {
  def apply(): AppConfig = {
    val c = ConfigFactory.load()
    AppConfig(c.getString("AWS_ACCESS_KEY"), c.getString("AWS_SECRET_KEY"), c.getString("AWS_BUCKET"))
  }
}
