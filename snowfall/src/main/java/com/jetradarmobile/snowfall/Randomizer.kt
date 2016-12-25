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

import java.lang.Math.abs
import java.util.Random

internal class Randomizer {
  private val random by lazy { Random() }

  fun randomDouble(max: Int): Double {
    return random.nextDouble() * (max + 1)
  }

  fun randomInt(min: Int, max: Int, gaussian: Boolean = false): Int {
    return randomInt(max - min, gaussian) + min
  }

  fun randomInt(max: Int, gaussian: Boolean = false): Int {
    if (gaussian) {
      return (abs(randomGaussian()) * (max + 1)).toInt()
    } else {
      return random.nextInt(max + 1)
    }
  }

  fun randomGaussian(): Double {
    val gaussian = random.nextGaussian() / 3 // more 99% of instances in range (-1, 1)
    return if (gaussian > -1 && gaussian < 1) gaussian else randomGaussian()
  }

  fun randomSignum(): Int {
    return if (random.nextBoolean()) 1 else -1
  }
}