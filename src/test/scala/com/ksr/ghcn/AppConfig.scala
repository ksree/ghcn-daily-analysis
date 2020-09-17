package com.ksr.ghcn

import com.ksr.ghcn.conf.AppConfig
import org.scalatest.FlatSpec

class AppConfig extends FlatSpec {

  val appConfig: conf.AppConfig = AppConfig()

  "config" should "provide the app config properties" in {
    assert(appConfig.awsBucket == "test")
    assert(appConfig.awsBucket == "test")
    assert(appConfig.awsBucket == "test")

  }

}
