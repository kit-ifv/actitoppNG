package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan

interface ModernizedHousehold {
    val members: List<IPerson>
    val areaType: AreaType
    val numberOfCars: Int

    val id: Int
}

fun interface HouseholdPlan {
    fun generateSchedules(household: ModernizedHousehold): Map<IPerson, MobilityPlan>
}

class StandardHouseholdPlan(private val planGeneration: MobilityPlanGeneration) : HouseholdPlan {
    override fun generateSchedules(household: ModernizedHousehold): Map<IPerson, MobilityPlan> {
        return household.members.associateWith { member ->
            planGeneration.generate(member)
        }
    }
}


//class MutableHousehold(override val areaType: AreaType, override val numberOfCars: Int): ModernizedHousehold {
//    override val members: MutableList<IPerson> = mutableListOf()
//}