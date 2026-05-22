package edu.kit.ifv.mobitopp.actitoppNG.performance

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DurationDay
import java.time.DayOfWeek
import kotlin.time.Duration

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
    fun convertWeekDay(durationDay: DurationDay): Int {
        return durationDay.weekday.value
    }

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
     *    0 EDUCATION(340),
     *    1 HOME(),
     *    2 LEISURE(130),
     *    3 SHOPPING(41),
     *    4 TRANSPORT(15),
     *    5 WORK(472);
     *
     */
    fun convertActivityType(activityType: ActivityType): Int {
        return activityType.ordinal
    }

    /**
     * Return an int representing which two hour block the duration is belonging to.
     *
     * 0..1h59m -> 0
     * 2h..3h59m -> 1
     */
    fun convertToTwoHourBlock(duration: Duration): Int {
        return (duration.inWholeHours / 2L).toInt()
    }

}