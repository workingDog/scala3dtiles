package com.kodekutters.cesium3dTiles

import play.api.libs.json.Json


// todo ---> do not use

/**
  * A Feature Table describes position and appearance properties for each feature in a tile.
  */
case class FeatureTable(t: String)

object FeatureTable {
  implicit val fmt = Json.format[FeatureTable]
}
