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

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build

@SuppressLint("NewApi")
internal fun Drawable.toBitmap(): Bitmap {
  return when (this) {
    is BitmapDrawable -> bitmap
    is VectorDrawable -> toBitmap()
    else -> throw IllegalArgumentException("Unsupported drawable type")
  }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal fun VectorDrawable.toBitmap(): Bitmap {
  val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  setBounds(0, 0, canvas.width, canvas.height)
  draw(canvas)
  return bitmap
}