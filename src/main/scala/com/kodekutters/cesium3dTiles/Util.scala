package com.kodekutters.cesium3dTiles

import java.io.{File, IOException, PrintWriter}
import play.api.libs.json.Json


/**
  * basic supporting utilities
  */
object Util {

  // constants
  val ADD = "add"
  val REPLACE = "replace"

  /**
    * write the Tileset document to a file.
    * @param outFile the file name to write to, if empty or missing output will be to System.out
    * @param tileset the Tileset document, i.e. the list of Tiles
    */
  def writeTilesetToFile(tileset: Tileset, outFile: Option[String] = None) = {
    val writer = if (outFile.isEmpty) new PrintWriter(System.out) else new PrintWriter(new File(outFile.get))
    try {
      writer.write(Json.prettyPrint(Json.toJson(tileset)))
    } catch {
      case e: IOException => e.printStackTrace()
    }
    finally {
      writer.flush()
      // close files, not System.out
      if (outFile.nonEmpty) writer.close()
    }
  }

  /**
    * write the (json) string representation of a Tileset document to a file.
    * @param outFile the file name to write to, if empty or missing output will be to System.out
    * @param tilesetjs the Tileset document as a (json) string
    */
  def writeJsToFile(tilesetjs: String, outFile: Option[String]) = {
    val writer = if (outFile.isEmpty) new PrintWriter(System.out) else new PrintWriter(new File(outFile.get))
    try {
      writer.write(tilesetjs)
    } catch {
      case e: IOException => e.printStackTrace()
    }
    finally {
      writer.flush()
      // close files, but not System.out
      if (outFile.nonEmpty) writer.close()
    }
  }


}