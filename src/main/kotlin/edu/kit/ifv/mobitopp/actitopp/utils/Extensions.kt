package edu.kit.ifv.mobitopp.actitopp.utils

import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.D
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.I
import java.util.SortedMap
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

inline operator fun Boolean.times(other: Double): Double = this.D * other
inline operator fun Boolean.times(other: Int): Int = this.I * other

fun Int.positiveModulus(modulo: Int): Int {
    val result = this % modulo
    return if (result < 0) result + modulo else result
}

fun <K, V> SortedMap<K, V>.lastValue(): V = this.getValue(lastKey())
fun <K, V> SortedMap<K, V>.firstValue(): V = this.getValue(firstKey())