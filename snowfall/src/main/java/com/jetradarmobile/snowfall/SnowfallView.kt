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
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.View

class SnowfallView(context: Context, attrs: AttributeSet) : View(context, attrs) {
  private val DEFAULT_SNOWFLAKES_NUM = 200
  private val DEFAULT_SNOWFLAKE_ALPHA_MIN = 150
  private val DEFAULT_SNOWFLAKE_ALPHA_MAX = 250
  private val DEFAULT_SNOWFLAKE_ANGLE_MAX = 10
  private val DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 2
  private val DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 8
  private val DEFAULT_SNOWFLAKE_SPEED_MIN = 2
  private val DEFAULT_SNOWFLAKE_SPEED_MAX = 8
  private val DEFAULT_SNOWFLAKES_FADING_ENABLED = false
  private val DEFAULT_SNOWFLAKES_ALREADY_FALLING = false

  private val snowflakesNum: Int
  private val snowflakeImage: Bitmap?
  private val snowflakeAlphaMin: Int
  private val snowflakeAlphaMax: Int
  private val snowflakeAngleMax: Int
  private val snowflakeSizeMinInPx: Int
  private val snowflakeSizeMaxInPx: Int
  private val snowflakeSpeedMin: Int
  private val snowflakeSpeedMax: Int
  private val snowflakesFadingEnabled: Boolean
  private val snowflakesAlreadyFalling: Boolean

  private lateinit var updateSnowflakesThread: UpdateSnowflakesThread
  private var snowflakes: Array<Snowflake>? = null

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView)
    try {
      snowflakesNum = a.getInt(R.styleable.SnowfallView_snowflakesNum, DEFAULT_SNOWFLAKES_NUM)
      snowflakeImage = a.getDrawable(R.styleable.SnowfallView_snowflakeImage)?.toBitmap()
      snowflakeAlphaMin = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMin, DEFAULT_SNOWFLAKE_ALPHA_MIN)
      snowflakeAlphaMax = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMax, DEFAULT_SNOWFLAKE_ALPHA_MAX)
      snowflakeAngleMax = a.getInt(R.styleable.SnowfallView_snowflakeAngleMax, DEFAULT_SNOWFLAKE_ANGLE_MAX)
      snowflakeSizeMinInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMin, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP))
      snowflakeSizeMaxInPx = a.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMax, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP))
      snowflakeSpeedMin = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMin, DEFAULT_SNOWFLAKE_SPEED_MIN)
      snowflakeSpeedMax = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMax, DEFAULT_SNOWFLAKE_SPEED_MAX)
      snowflakesFadingEnabled = a.getBoolean(R.styleable.SnowfallView_snowflakesFadingEnabled, DEFAULT_SNOWFLAKES_FADING_ENABLED)
      snowflakesAlreadyFalling = a.getBoolean(R.styleable.SnowfallView_snowflakesAlreadyFalling, DEFAULT_SNOWFLAKES_ALREADY_FALLING)
    } finally {
      a.recycle()
    }

  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    updateSnowflakesThread = UpdateSnowflakesThread()
  }

  override fun onDetachedFromWindow() {
    updateSnowflakesThread.quit()
    super.onDetachedFromWindow()
  }

  private fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    snowflakes = createSnowflakes()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (changedView === this && visibility == GONE) {
      snowflakes?.forEach { it.reset() }
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    if (isInEditMode) {
      return
    }
    snowflakes?.forEach { it.draw(canvas) }
    updateSnowflakes()
  }

  private fun createSnowflakes(): Array<Snowflake> {
    val snowflakeParams = Snowflake.Params(
        parentWidth = width,
        parentHeight = height,
        image = snowflakeImage,
        alphaMin = snowflakeAlphaMin,
        alphaMax = snowflakeAlphaMax,
        angleMax = snowflakeAngleMax,
        sizeMinInPx = snowflakeSizeMinInPx,
        sizeMaxInPx = snowflakeSizeMaxInPx,
        speedMin = snowflakeSpeedMin,
        speedMax = snowflakeSpeedMax,
        fadingEnabled = snowflakesFadingEnabled,
        alreadyFalling = snowflakesAlreadyFalling)
    return Array(snowflakesNum, { Snowflake(snowflakeParams) })
  }

  private fun updateSnowflakes() {
    updateSnowflakesThread.handler.post {
      snowflakes?.forEach { it.update() }
      postInvalidateOnAnimation()
    }
  }

  private class UpdateSnowflakesThread : HandlerThread("SnowflakesComputations") {
    val handler by lazy { Handler(looper) }

    init {
      start()
    }
  }
}