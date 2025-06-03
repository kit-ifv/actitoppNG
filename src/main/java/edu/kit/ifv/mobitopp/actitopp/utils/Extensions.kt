package edu.kit.ifv.mobitopp.actitopp.utils

import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Similar behaviour to [Duration.inWholeMinutes] but it always rounds up instead of down.
 */
val Duration.ceilWholeMinutes:Int get() {
    val double = this.toDouble(DurationUnit.MINUTES)
    return ceil(double).toInt()
}