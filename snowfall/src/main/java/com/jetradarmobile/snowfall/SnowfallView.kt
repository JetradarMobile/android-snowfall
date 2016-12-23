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
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList
import java.util.Random

class SnowfallView(context: Context, attrs: AttributeSet) : View(context, attrs) {
  private val DEFAULT_SNOWFLAKES_NUM = 150
  private val DEFAULT_SNOWFLAKE_ALPHA_MIN = 150
  private val DEFAULT_SNOWFLAKE_ALPHA_MAX = 250
  private val DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 2
  private val DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 8
  private val DEFAULT_SNOWFLAKE_FADEOUT = false

  private val snowflakesNum: Int
  private val snowflakeAlphaMin: Int
  private val snowflakeAlphaMax: Int
  private val snowflakeSizeMinInPx: Int
  private val snowflakeSizeMaxInPx: Int
  private val snowflakeFadeout: Boolean

  private val snowflakes: MutableList<Snowflake>
  private val updateThread by lazy { UpdateThread() }

  init {
    updateThread.start()

    val a = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView)
    snowflakesNum = a.getInt(R.styleable.SnowfallView_snowflakes_num, DEFAULT_SNOWFLAKES_NUM)
    snowflakeAlphaMin = a.getInt(R.styleable.SnowfallView_snowflake_alpha_min, DEFAULT_SNOWFLAKE_ALPHA_MIN)
    snowflakeAlphaMax = a.getInt(R.styleable.SnowfallView_snowflake_alpha_max, DEFAULT_SNOWFLAKE_ALPHA_MAX)
    snowflakeSizeMinInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflake_size_min, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP))
    snowflakeSizeMaxInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflake_size_max, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP))
    snowflakeFadeout = a.getBoolean(R.styleable.SnowfallView_snowflake_fadeout, DEFAULT_SNOWFLAKE_FADEOUT)
    a.recycle()

    snowflakes = ArrayList(snowflakesNum)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    if (w != oldw || h != oldh) {
      snowflakes.clear()
      snowflakes.addAll(Array(snowflakesNum, { createSnowflake() }))
    }
  }

  private fun createSnowflake(): Snowflake {
    val position = Point(random(width), random(height))
    val size = random(snowflakeSizeMinInPx, snowflakeSizeMaxInPx)
    val speed = size / 3
    val angle = 0F
    val alpha = random(snowflakeAlphaMin, snowflakeAlphaMax)
    return Snowflake(position = position, size = size, speed = speed, angle = angle, alpha = alpha, fadeout = snowflakeFadeout)
  }

  private fun random(min: Int, max: Int): Int {
    return random(max - min + 1) + min
  }

  private fun random(max: Int): Int {
    return Random().nextInt(max)
  }

  private fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    snowflakes.forEach { it.draw(canvas) }
  }

  private inner class UpdateThread : Thread() {
    private val FPS = 10L

    override fun run() {
      while (true) {
        try { Thread.sleep(FPS) } catch (ignored: InterruptedException) {}
        snowflakes.forEach { it.update(width, height) }
        postInvalidateOnAnimation()
      }
    }
  }
}