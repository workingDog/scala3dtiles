/**
  * Cesium 3DTiles.
  *
  * The Cesium specification for streaming massive heterogeneous 3D geospatial datasets,
  * as described at: https://github.com/AnalyticalGraphicsInc/3d-tiles
  *
  * see also: http://cesiumjs.org/2015/08/10/Introducing-3D-Tiles/
  * 3DTiles are an open specification for streaming massive heterogeneous 3D geospatial datasets.
  *
  */

package com.kodekutters.cesium3dTiles

import play.api.libs.json._
import play.api.libs.functional.syntax._


  /**
    * A bounding volume that encloses a tile or its contents. At least one property is required.
    * If more than one property is defined, the runtime can determine which to use.
    *
    * @param box    An array of 12 numbers that define an oriented bounding box in WGS84 coordinates.
    *               The first three elements define the x, y, and z values for the center of the box.
    *               The next three elements (with indices 3, 4, and 5) define the x axis direction and length.
    *               The next three elements (indices 6, 7, and 8) define the y axis direction and length.
    *               The last three elements (indices 9, 10, and 11) define the z axis direction and length.
    * @param region An array of six numbers that define a bounding geographic region with
    *               the order [west, south, east, north, minimum height, maximum height].
    *               Longitudes and latitudes are in radians, and heights are in meters above (or below) the WGS84 ellipsoid.
    * @param sphere An array of four numbers that define a bounding sphere.
    *               The first three elements define the x, y, and z values for the center of the sphere.
    *               The last element (with index 3) defines the radius in meters.
    */
  case class BoundingVolume(box: Option[Array[Double]], region: Option[Array[Double]], sphere: Option[Array[Double]])

  object BoundingVolume {
    implicit val fmt = Json.format[BoundingVolume]
  }

  /**
    * Metadata about the tile's content and a link to the content.
    *
    * @param boundingVolume An optional bounding volume that tightly enclosing just the tile's contents.
    *                       This is used for replacement refinement; tile.boundingVolume provides
    *                       spatial coherence and tile.content.boundingVolume enables tight view frustum culling.
    *                       When this is omitted, tile.boundingVolume is used
    * @param url            A string that points to the tile's contents with an absolute or relative url.
    *                       When the url is relative, it is relative to the referring tileset.json.
    *                       The file extension of content.url defines the tile format.
    *                       The core 3D Tiles spec supports the following tile formats: Batched 3D Model (*.b3dm),
    *                       Instanced 3D Model (*.i3dm), Composite (*.cmpt), and 3D Tiles TileSet (*.json)
    */
  case class Content(boundingVolume: Option[BoundingVolume], url: String)

  object Content {
    implicit val fmt = Json.format[Content]
  }

  // todo refine --> "enum" : ["add", "replace"]
  /**
    * A tile in a 3D Tiles tileset.
    *
    * @param boundingVolume The bounding volume that encloses the tile.
    * @param geometricError The error, in meters, introduced if this tile is rendered and its children are not. At runtime, the geometric error is used to compute Screen-Space Error (SSE), i.e., the error measured in pixels.
    * @param refine         Specifies if additive or replacement refinement is used when traversing the tileset for rendering.  This property is required for the root tile of a tileset; it is optional for all other tiles.  The default is to inherit from the parent tile.
    * @param content        Metadata about the tile's content and a link to the content. When this is omitted the tile is just used for culling. This is required for leaf tiles.
    * @param children       An array of objects that define child tiles. Each child tile has a box fully enclosed by its parent tile's box and, generally, a geometricError less than its parent tile's geometricError. For leaf tiles, the length of this array is zero, and children may not be defined.
    */
  case class Tile(boundingVolume: BoundingVolume, geometricError: Double, refine: Option[String],
                  content: Option[Content], children: Option[Array[Tile]])

  object Tile {

    implicit val tileFormat: Format[Tile] = (
      (__ \ 'boundingVolume).format[BoundingVolume] and
        (__ \ 'geometricError).format[Double] and
        (__ \ 'refine).formatNullable[String] and
        (__ \ 'content).formatNullable[Content] and
        (__ \ 'children).lazyFormatNullable(implicitly[Format[Array[Tile]]])
      ) (Tile.apply, unlift(Tile.unapply))

  }

  /**
    * Metadata about the entire tileset.
    *
    * @param version        The 3D Tiles version.  The version defines the JSON schema for tileset.json and the base set of tile formats.
    * @param tilesetVersion Application-specific version of this tileset, e.g., for when an existing tileset is updated
    */
  case class Asset(version: String, tilesetVersion: Option[String])

  object Asset {
    implicit val fmt = Json.format[Asset]
  }

  /**
    * A dictionary object of metadata about per-feature properties.
    *
    * @param minimum The minimum value of this property of all the features in the tileset.
    * @param maximum The maximum value of this property of all the features in the tileset.
    */
  case class MinMax(minimum: Double, maximum: Double)

  object MinMax {
    implicit val fmt = Json.format[MinMax]
  }

  /**
    * Properties
    */
  case class Properties(id: Option[MinMax], Longitude: Option[MinMax], Latitude: Option[MinMax], Height: Option[MinMax])

  object Properties {
    implicit val fmt = Json.format[Properties]
  }

  /**
    * A 3D Tiles tileset.
    *
    * @param asset          Metadata about the entire tileset.
    * @param properties     A dictionary object of metadata about per-feature properties.
    * @param geometricError The error, in meters, introduced if this tileset is not rendered.
    *                       At runtime, the geometric error is used to compute Screen-Space Error
    *                       (SSE), i.e., the error measured in pixels.
    * @param root           The root node.
    */
  case class Tileset(asset: Asset, properties: Option[Properties], geometricError: Double, root: Tile)

  object Tileset {
    implicit val fmt = Json.format[Tileset]
  }


