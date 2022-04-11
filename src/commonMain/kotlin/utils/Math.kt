package utils

import com.soywiz.kds.random.FastRandom.Companion.nextDouble

fun randomInRange(min: Double, max: Double): Double {
    return nextDouble() * (max - min) + min
}
