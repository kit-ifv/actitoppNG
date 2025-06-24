package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender
import edu.kit.ifv.mobitopp.actitopp.modernization.IHousehold

interface IPerson {
    val employment: Employment
    val age: Int
    val gender: Gender
    val maxCommute: Double
    val isAllowedToWork: Boolean
    val commutingdistance_work: Double
    val commutingdistance_education: Double

    val id: Int
    val household: IHousehold
    fun isAnywayEmployed(): Boolean
    fun isinEducation(): Boolean
    fun spawnRandomGenerator(offset: Long = 0L): RNGHelper {
        return RNGHelper(id.toLong() + offset)
    }
}