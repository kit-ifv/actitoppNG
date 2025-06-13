package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender

interface IPerson {
    val children0_10: Int
    val children_u18: Int
    val employment: Employment
    val age: Int
    val areatype: AreaType
    val gender: Gender
    val maxCommute: Double
    val numberofcarsinhousehold: Int
    val isAllowedToWork: Boolean
    val commutingdistance_work: Double
    val commutingdistance_education: Double

    val id: Int

    fun isAnywayEmployed(): Boolean
    fun isinEducation(): Boolean
    fun spawnRandomGenerator(offset: Long = 0L): RNGHelper {
        return RNGHelper(id.toLong() + offset)
    }
}