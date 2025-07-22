package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.MobilityPlan
import kotlin.random.Random

fun interface HouseholdPlanGeneration {
    context(rng: Random)
    fun generateSchedules(household: Household): Map<Person, MobilityPlan>
}

class StandardHouseholdPlanGeneration(private val planGeneration: MobilityPlanGeneration = DefaultPlanGeneration()) :
    HouseholdPlanGeneration {
    context(rng: Random)
    override fun generateSchedules(household: Household): Map<Person, MobilityPlan> {
        return household.members.associateWith { member ->
            planGeneration.generate(member)
        }
    }
}