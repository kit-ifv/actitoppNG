package edu.kit.ifv.mobitopp.actitoppNG.performance

import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import java.time.DayOfWeek
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Many Utility functions run over the same fields with repeated boolean checks. This causes the
 * utility function to bloat with if else statements and makes it really difficult for the compiler
 * to inline and optimize the utility function.
 *
 * This object should encapsulate common repeated boolean checks, and convert them to a unified integer
 * encoding, which the utility function can then utilize with a integer when(i) block rather than a
 * largely nested conditions block. Take Weekdays for example, many utility functions execute the identical
 * check. If Monday use paramMo, if Tuesday use paramDi, etc. Weekdays, and many other attributes are
 * well partitioned, and can thus be optimized.
 *
 *
 */
object UtilityConverter {
    /**
     * Converts the dayofweek to the integer representation of value. Mo = 1, So = 7
     */
    fun convertWeekDay(dayOfWeek: DayOfWeek): Int {
        return dayOfWeek.value
    }

    /**
     *     0 FULLTIME(1),
     *     1 PARTTIME(2),
     *     2 UNOCCUPIED(3),
     *     3 STUDENT(4),
     *     4  VOCATIONAL(5),
     *     5 HOUSEKEEPER(6),
     *     6 RETIRED(7),
     *     7 UNKNOWN_21(21),
     *     8 MARGINAL(22),
     *     9 STUDENT_PRIMARY(40),
     *     10 STUDENT_SECONDARY(41),
     *     11 STUDENT_TERTIARY(42),
     *     12 DEFINITELY_UNKNOWN(Int.MIN_VALUE);
     */
    fun convertEmployment(employment: Employment): Int {
        return employment.ordinal
    }

    /**
     * Returns a n Int representing the 15 minute block the duration is belonging to:
     *
     * 0..14m59s -> 0
     * 15..29m59s -> 1
     *
     */
    fun convertToFifteenMinuteBlock(duration: Duration): Int {
        require(duration != Duration.ZERO) {
            "Cannot handle duration zero, because original Actitopp never caught that in the utility functions. "
        }
        return (duration.inWholeMinutes.toInt() / 15)
    }

    fun convertToOneHourBlock(duration: Duration): Int {
        return duration.inWholeHours.toInt()
    }
}


object AgeEncoder {
    const val isAged1OrLess = -1
    const val isAged10To17 = 0
    const val isAged18To25 = 1
    const val isAged26To35 = 2
    const val isAged36To50 = 3
    const val isAged51To60 = 4
    const val isAged61To70 = 5
    const val isAged71ToInfty = 6
    /**
     * Encodes Age the following way.
     * -1 -> <10
     * 0 -> 10 - 17
     * 1 -> 18 - 25
     * 2 -> 25 - 34
     * 3 -> 35 - 50
     * 4 -> 51 - 60
     * 5 -> 61 - 70
     * 6 -> >= 71
     */
    fun encode(age: Int): Int {
        return when (age) {
            in 10..17 -> isAged10To17
            in 18..25 -> isAged18To25
            in 26..35 -> isAged26To35
            in 36..50 -> isAged36To50
            in 51..60 -> isAged51To60
            in 61..70 -> isAged61To70
            in 71..Int.MAX_VALUE -> isAged71ToInfty
            else -> isAged1OrLess
        }
    }
}

/**
 * Integer encoding for Commute Distances
 */
object KilometerEncoder {
    const val negativeDistance = -1
    const val In0To5km = 0
    const val In5To10km = 1
    const val In10To20km = 2
    const val In20To50km = 3
    const val Over50km = 4

    /**
     * Encodes commute distance
     */
    fun convert(distanceKilometers: Double): Int {
        return when (distanceKilometers) {
            in 0.0..5.0 -> In0To5km
            in 5.0..10.0 -> In5To10km
            in 10.0..20.0 -> In10To20km
            in 2.0..50.0 -> In20To50km
            else -> {
                if(distanceKilometers > 50.0) Over50km
                else negativeDistance
            }
        }
    }
}

/**
 * Integer encoding for Time. Encodes it into 2h blocks
 */
@Suppress("unused")
object TimeBlockEncoder {
    const val NegativeHours = -1
    const val In0to2Hours = 0
    const val In2to4Hours = 1
    const val In4to6Hours = 2
    const val In6to8Hours = 3
    const val In8to10Hours = 4
    const val In10to12Hours = 5
    const val In12to14Hours = 6
    const val MoreThan14Hours = 7
    /**
     * Encodes durations in two hour blocks
     * ```
     * NegativeHours = -1
     * 0to2Hours = 0
     * 2to4Hours = 1
     * 4to6Hours = 2
     * 6to8Hours = 3
     * 8to10Hours = 4
     * 10to12Hours = 5
     * 12to14Hours = 6
     * MoreThan14Hours = 7
     * ```
     */
    fun convert(duration: Duration): Int {
        if (duration < 0.minutes) return -1
        if (duration > 14.hours) return 7

        return (duration.inWholeHours.toInt() / 2)
    }
}

/**
 * Converts durations to integer codes representing time intervals.
 * -1 -> <1m
 * 0 -> 1..14m59s
 * 1 -> 15..29m59s
 * 2 -> 30..59m59s
 * 3 -> 60..119m
 * 4 -> 120..179m
 * 5 -> ... only 60 min steps after this.
 */
object WeightedTimeConverter {
    const val LessThan1Minute = -1
    const val In1To14Minutes = 0
    const val In15To29Minutes = 1
    const val In30To59Minutes = 2
    const val In60To119Minutes = 3
    const val In120To179Minutes = 4
    const val In180To239Minutes = 5
    const val In240To299Minutes = 6
    const val In300To359Minutes = 7
    const val In360To419Minutes = 8
    const val In420To479Minutes = 9

    /**
     * Returns an int representing the duration this block is in.
     * -1 -> <1m
     * 0 -> 1..14m59s
     * 1 -> 15..29m59s
     * 2 -> 30..59m59s
     * 3 -> 60..119m
     * 4 -> 120..179m
     * 5 -> ... only 60 min steps after this.
     */
     fun convert(duration: Duration): Int {
        if (duration < 1.minutes) return -1
        return if (duration < 15.minutes) 0
        else if (duration < 30.minutes) 1
        else if (duration < 60.minutes) 2
        else (duration.inWholeMinutes.toInt() / 60) + 2
     }
}