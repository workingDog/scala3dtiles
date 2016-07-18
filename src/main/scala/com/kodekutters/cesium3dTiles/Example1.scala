package com.kodekutters.czml


import com.kodekutters.cesium3dTiles.{Tileset, Util}
import play.api.libs.json._

import scala.io.Source
import scala.language.implicitConversions


/**
  * a basic example  ... todo
  */
object Example1 {
  def main(args: Array[String]) {

    // read a tileset json document from a file
    val jsonDoc = Source.fromFile("/Users/...../tileset.json").mkString

    // get the Tileset object from the json document
    val tilesetOpt = Json.fromJson[Tileset](Json.parse(jsonDoc)).asOpt

    tilesetOpt match {
      case Some(s) => Util.writeTilesetToFile(s)

      case None => println("\n---> error processing the Tileset\n")
    }

  }
}