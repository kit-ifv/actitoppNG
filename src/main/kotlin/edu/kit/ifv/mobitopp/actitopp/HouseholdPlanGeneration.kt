package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan

fun interface HouseholdPlanGeneration {
    fun generateSchedules(household: IHousehold): Map<IPerson, MobilityPlan>
}

class StandardHouseholdPlanGeneration(private val planGeneration: MobilityPlanGeneration = DefaultPlanGeneration()) :
    HouseholdPlanGeneration {
    override fun generateSchedules(household: IHousehold): Map<IPerson, MobilityPlan> {
        return household.members.associateWith { member ->
            planGeneration.generate(member)
        }
    }
}