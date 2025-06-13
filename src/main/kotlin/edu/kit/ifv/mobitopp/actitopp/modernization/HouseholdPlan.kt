package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan

fun interface HouseholdPlan {
    fun generateSchedules(household: IHousehold): Map<IPerson, MobilityPlan>
}

class StandardHouseholdPlan(private val planGeneration: MobilityPlanGeneration) : HouseholdPlan {
    override fun generateSchedules(household: IHousehold): Map<IPerson, MobilityPlan> {
        return household.members.associateWith { member ->
            planGeneration.generate(member)
        }
    }
}