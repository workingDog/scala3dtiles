package com.kodekutters.cesium3dTiles

import play.api.libs.json._
import scala.collection.mutable

// todo ---> do not use

/**
  * for use in BatchTable
  * @param byteOffset
  * @param componentType
  * @param `type`
  */
case class BinaryBodyReference(byteOffset: Int, componentType: String, `type`: String)

object BinaryBodyReference {
  implicit val fmt = Json.format[BinaryBodyReference]
}

/**
  * A set of properties defining application-specific metadata for features in a tile.
  *
  * @param extra
  * @param prop
  */
case class BatchTable(extra: Option[scala.collection.mutable.ListMap[String, Array[String]]] = None,
                      prop: scala.collection.mutable.ListMap[String, BinaryBodyReference])
// todo enums, fix the getOrElse with default values and extra
object BatchTable {
  val theReads = new Reads[BatchTable] {
    def reads(json: JsValue): JsResult[BatchTable] = {
      json match {
        case js: JsObject =>
          val theListMap = mutable.ListMap.empty ++= js.fields.collect {
            case (key, JsObject(value)) => key -> BinaryBodyReference.fmt.reads(JsObject(value)).getOrElse(new BinaryBodyReference(0, "",""))
          }
          JsSuccess(new BatchTable(None, theListMap))

        case x => JsError(s"Could not read BatchTable : $x")
      }
    }
  }

  // todo
  val theWrites = new Writes[BatchTable] {
    def writes(btable: BatchTable) = {
      val listp = for (p <- btable.prop) yield p._1 -> Json.toJson(p._2)
      if(btable.extra.isDefined) {
        val listx = for (x <- btable.extra.get) yield x._1 -> Json.toJson(x._2)
        JsObject(listp)  // todo JsObject(listx)
      } else
        JsObject(listp)
    }
  }

  implicit val fmt: Format[BatchTable] = Format(theReads, theWrites)
}
