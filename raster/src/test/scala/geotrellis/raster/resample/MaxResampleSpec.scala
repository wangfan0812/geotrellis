/*
 * Copyright 2016 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.raster.resample

import geotrellis.raster._
import geotrellis.vector.Extent

import org.scalatest._

class MaxResampleSpec extends FunSpec with Matchers {

  describe("it should resample to nodata when only nodata in tile") {

    it("should for a nodata integer tile compute nodata as max value") {
      val tile = IntArrayTile(Array(NODATA, NODATA, NODATA,
                                    NODATA, NODATA, NODATA,
                                    NODATA, NODATA, NODATA), 3, 3)
      val extent = Extent(0, 0, 10, 10)
      val cellsize = CellSize(extent, 10, 10)
      tile.resample(extent, TargetDimensions(1, 1), Max).get(0, 0) should be (NODATA)
    }

    it("should for a nodata double tile compute nodata as max value") {
      val tile = DoubleArrayTile(Array(NODATA, NODATA, NODATA,
                                       NODATA, NODATA, NODATA,
                                       NODATA, NODATA, NODATA), 3, 3)
      val extent = Extent(0, 0, 10, 10)
      val cellsize = CellSize(extent, 10, 10)
      tile.resample(extent, TargetDimensions(1, 1), Max).get(0, 0) should be (NODATA)
    }

    it("should for an integer tile compute the correct maximum value") {
      val tile = ArrayTile(Array(1, 2, 3, 4, 5, 6, 7, 9, 72), 3, 3)
      val extent = Extent(0, 0, 3, 3)
      val cellsize = CellSize(extent, 3, 3)
      tile.resample(extent, TargetDimensions(1, 1), Max).get(0, 0) should be (72)
    }

    it("should for a double tile compute the correct maximum value") {
      val tile = DoubleArrayTile(Array(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 100.1, 0.19, 0.19), 3, 3)
      val extent = Extent(0, 0, 3, 3)
      val cellsize = CellSize(extent, 3, 3)
      tile.resample(extent, TargetDimensions(1, 1), Max).getDouble(0, 0) should be (100.1)
    }

    // This test is necessitated because of Double.NaN's always being max and min according to scala
    it("should for a double tile compute the correct maximum value - ignoring the nodata value") {
      val tile = DoubleArrayTile(Array(NODATA, 0.2, 0.3, 0.4, 0.5, 0.6, 100.1, 0.19, 0.19), 3, 3)
      val extent = Extent(0, 0, 3, 3)
      val cellsize = CellSize(extent, 3, 3)
      tile.resample(extent, TargetDimensions(1, 1), Max).getDouble(0, 0) should be (100.1)
    }
  }
}
