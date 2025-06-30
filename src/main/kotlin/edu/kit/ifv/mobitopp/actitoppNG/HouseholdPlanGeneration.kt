package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.MobilityPlan

fun interface HouseholdPlanGeneration {
    fun generateSchedules(household: Household): Map<Person, MobilityPlan>
}

class StandardHouseholdPlanGeneration(private val planGeneration: MobilityPlanGeneration = DefaultPlanGeneration()) :
    HouseholdPlanGeneration {
    override fun generateSchedules(household: Household): Map<Person, MobilityPlan> {
        return household.members.associateWith { member ->
            planGeneration.generate(member)
        }
    }
}