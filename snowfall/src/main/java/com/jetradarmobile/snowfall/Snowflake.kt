/*
 * Copyright (C) 2016 JetRadar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetradarmobile.snowfall

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Point
import java.util.Random

internal class Snowflake(val position: Point, val size: Int, val speed: Int, val angle: Float, val alpha: Int, val fadeout: Boolean) {
  private val paint by lazy {
    Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = Color.rgb(255, 255, 255)
      alpha = this@Snowflake.alpha
      style = Style.FILL
    }
  }

  fun update(width: Int, height: Int) {
    var x = position.x
    var y = position.y + speed
    if (y > height) {
      x = Random().nextInt(width)
      y = -size - 1
    }

    if (fadeout) {
      paint.alpha = (alpha * ((height.toFloat() - y.toFloat()) / (height.toFloat()))).toInt()
    }

    position.set(x, y)
  }

  fun draw(canvas: Canvas) {
    canvas.drawCircle(position.x.toFloat(), position.y.toFloat(), size.toFloat(), paint)
  }
}