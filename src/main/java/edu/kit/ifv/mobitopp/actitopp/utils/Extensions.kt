package edu.kit.ifv.mobitopp.actitopp.utils

import kotlin.math.ceil
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Similar behaviour to [Duration.inWholeMinutes] but it always rounds up instead of down.
 */
val Duration.ceilWholeMinutes:Int get() {
    val double = this.toDouble(DurationUnit.MINUTES)
    return ceil(double).toInt()
}

operator fun Duration.rem(that: Duration): Duration {
    return (this.toDouble(DurationUnit.MINUTES) % that.toDouble(DurationUnit.MINUTES)).minutes
}

fun Duration.ceil(unit: DurationUnit): Duration {
    return ceil(this.toDouble(unit)).toDuration(unit)
}

fun Duration.round(unit: DurationUnit) : Duration {
    return this.toDouble(unit).roundToLong().toDuration(unit)
}

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Duration): Duration {
    var sum: Duration = Duration.ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}