package com.ksr.ghcn.transformer

import com.ksr.ghcn.domain.GHCN_D_RAW
import org.apache.spark.sql.Dataset

object ghcnDTransform {

  def transformGHCNDRaw(in: GHCN_D_RAW) = {
    in.id
  }
}
