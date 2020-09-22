package com.ksr.ghcn.transformer

import org.scalatest.FlatSpec

class ghcnDTransformTest extends FlatSpec {

  "getDate" should "convert string to date type" in {
    assert(ghcnDTransform.getDate("17880101") === java.sql.Date.valueOf("1788-01-01"))
  }
}
