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

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isParttime(): Boolean {
    return this == Employment.PARTTIME || this == Employment.UNKNOWN_21 || this == Employment.MARGINAL
}
val isParttime: EnumSet<Employment> = EnumSet.of(
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL
)

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isEarning() =
    this == Employment.FULLTIME || this == Employment.PARTTIME || this == Employment.UNKNOWN_21 || this == Employment.MARGINAL
val isEarning: EnumSet<Employment> = EnumSet.of(
    Employment.FULLTIME,
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL)

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isNotEarning() = this == Employment.UNOCCUPIED || this == Employment.HOUSEKEEPER
val isNotEarning: EnumSet<Employment>  = EnumSet.of(
    Employment.UNOCCUPIED,
    Employment.HOUSEKEEPER
)

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isEmployedAnywhere() = this.isEarning() || this == Employment.VOCATIONAL
val isEmployedAnywhere: EnumSet<Employment> = EnumSet.of(
    Employment.FULLTIME,
    Employment.PARTTIME,
    Employment.UNKNOWN_21,
    Employment.MARGINAL,
    Employment.VOCATIONAL)

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isStudent() =
    this == Employment.STUDENT || this == Employment.STUDENT_PRIMARY || this == Employment.STUDENT_SECONDARY || this == Employment.STUDENT_TERTIARY
val isStudent: EnumSet<Employment> = EnumSet.of(
    Employment.STUDENT,
    Employment.STUDENT_PRIMARY,
    Employment.STUDENT_SECONDARY,
    Employment.STUDENT_TERTIARY)

@Deprecated("Use the value with the same name instead. These are Enumsets and have faster lookup.")
fun Employment.isStudentOrAzubi() = this == Employment.VOCATIONAL || this.isStudent()
val isStudentOrAzubi: EnumSet<Employment> = EnumSet.of(
    Employment.STUDENT,
    Employment.STUDENT_PRIMARY,
    Employment.STUDENT_SECONDARY,
    Employment.STUDENT_TERTIARY,
    Employment.VOCATIONAL)