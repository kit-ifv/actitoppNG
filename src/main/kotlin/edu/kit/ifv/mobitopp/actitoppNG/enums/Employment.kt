package edu.kit.ifv.mobitopp.actitoppNG.enums

import java.util.EnumSet

/**
 * TODO I have no Idea what the unknown codes should be, I can only implement the function found in the codebase. Robin, 25
 */
enum class Employment(val code: Int) {
    FULLTIME(1),
    PARTTIME(2),
    UNOCCUPIED(3),
    STUDENT(4),
    VOCATIONAL(5),
    HOUSEKEEPER(6),
    RETIRED(7),
    UNKNOWN_21(21),
    MARGINAL(22),
    STUDENT_PRIMARY(40),
    STUDENT_SECONDARY(41),
    STUDENT_TERTIARY(42),
    DEFINITELY_UNKNOWN(Int.MIN_VALUE);

    companion object {
        fun fromInt(code: Int): Employment = entries.firstOrNull { it.code == code } ?: DEFINITELY_UNKNOWN

    }
}

fun Employment.isParttime() = this in isParttime
val isParttime: EnumSet<Employment> = EnumSet.of(
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL
)

fun Employment.isEarning() = this in isEarning
val isEarning: EnumSet<Employment> = EnumSet.of(
    Employment.FULLTIME,
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL)


fun Employment.isNotEarning() = this in isNotEarning
val isNotEarning: EnumSet<Employment>  = EnumSet.of(
    Employment.UNOCCUPIED,
    Employment.HOUSEKEEPER
)

fun Employment.isEmployedAnywhere() = this in isEmployedAnywhere
val isEmployedAnywhere: EnumSet<Employment> = EnumSet.of(
    Employment.FULLTIME,
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL,
    Employment.VOCATIONAL)

fun Employment.isStudent() = this in isStudent
val isStudent: EnumSet<Employment> = EnumSet.of(
    Employment.STUDENT,
    Employment.STUDENT_PRIMARY,
    Employment.STUDENT_SECONDARY,
    Employment.STUDENT_TERTIARY)

fun Employment.isStudentOrAzubi() = this in isStudentOrAzubi
val isStudentOrAzubi: EnumSet<Employment> = EnumSet.of(
    Employment.STUDENT,
    Employment.STUDENT_PRIMARY,
    Employment.STUDENT_SECONDARY,
    Employment.STUDENT_TERTIARY,
    Employment.VOCATIONAL)