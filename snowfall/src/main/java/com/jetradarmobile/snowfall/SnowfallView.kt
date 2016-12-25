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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList

class SnowfallView(context: Context, attrs: AttributeSet) : View(context, attrs) {
  private val DEFAULT_SNOWFLAKES_NUM = 200
  private val DEFAULT_SNOWFLAKE_ALPHA_MIN = 150
  private val DEFAULT_SNOWFLAKE_ALPHA_MAX = 250
  private val DEFAULT_SNOWFLAKE_ANGLE_MAX = 10
  private val DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 2
  private val DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 8
  private val DEFAULT_SNOWFLAKE_SPEED_MIN = 2
  private val DEFAULT_SNOWFLAKE_SPEED_MAX = 8
  private val DEFAULT_SNOWFLAKE_FADING_ENABLED = false

  private val snowflakesNum: Int
  private val snowflakeImage: Bitmap?
  private val snowflakeAlphaMin: Int
  private val snowflakeAlphaMax: Int
  private val snowflakeAngleMax: Int
  private val snowflakeSizeMinInPx: Int
  private val snowflakeSizeMaxInPx: Int
  private val snowflakeSpeedMin: Int
  private val snowflakeSpeedMax: Int
  private val snowflakeFadingEnabled: Boolean

  private val snowflakes: MutableList<Snowflake>
  private val updateSnowflakesThread: UpdateSnowflakesThread

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView)
    snowflakesNum = a.getInt(R.styleable.SnowfallView_snowflakesNum, DEFAULT_SNOWFLAKES_NUM)
    snowflakeImage = a.getDrawable(R.styleable.SnowfallView_snowflakeImage)?.toBitmap()
    snowflakeAlphaMin = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMin, DEFAULT_SNOWFLAKE_ALPHA_MIN)
    snowflakeAlphaMax = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMax, DEFAULT_SNOWFLAKE_ALPHA_MAX)
    snowflakeAngleMax = a.getInt(R.styleable.SnowfallView_snowflakeAngleMax, DEFAULT_SNOWFLAKE_ANGLE_MAX)
    snowflakeSizeMinInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMin, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP))
    snowflakeSizeMaxInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMax, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP))
    snowflakeSpeedMin = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMin, DEFAULT_SNOWFLAKE_SPEED_MIN)
    snowflakeSpeedMax = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMax, DEFAULT_SNOWFLAKE_SPEED_MAX)
    snowflakeFadingEnabled = a.getBoolean(R.styleable.SnowfallView_snowflakeFadingEnabled, DEFAULT_SNOWFLAKE_FADING_ENABLED)
    a.recycle()

    snowflakes = ArrayList(snowflakesNum)

    updateSnowflakesThread = UpdateSnowflakesThread()
    updateSnowflakesThread.start()
  }

  private fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    snowflakes.clear()
    val snowflakeParams = Snowflake.Params(
        parentWidth = width, parentHeight = height, image = snowflakeImage,
        alphaMin = snowflakeAlphaMin, alphaMax = snowflakeAlphaMax, angleMax = snowflakeAngleMax,
        sizeMinInPx = snowflakeSizeMinInPx, sizeMaxInPx = snowflakeSizeMaxInPx,
        speedMin = snowflakeSpeedMin, speedMax = snowflakeSpeedMax,
        fadingEnabled = snowflakeFadingEnabled)
    snowflakes.addAll(Array(snowflakesNum, { Snowflake(snowflakeParams) }))
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    snowflakes.forEach { it.draw(canvas) }
  }

  private inner class UpdateSnowflakesThread : Thread() {
    private val FPS = 10L

    override fun run() {
      while (true) {
        try { Thread.sleep(FPS) } catch (ignored: InterruptedException) {}
        snowflakes.forEach { it.update() }
        postInvalidateOnAnimation()
      }
    }
  }
}