package edu.kit.ifv.mobitopp.actitopp.steps.step2

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType

fun interface GenerateMainActivities {
    fun generate(input: PersonWithRoutine, days: List<HDay>): List<ActivityType>

    fun generate(person: ActitoppPerson, routine: WeekRoutine, days: List<HDay>) = generate(PersonWithRoutine(person, routine), days)
}

/**
 * This is the logic separation that ususally occurs in the legacy code using the Configuration.coordinated_modelling
 * variable. In order to replace this variable, we implement the behaviour of each step with .coordinated modelling
 * using own implementations.
 */
class GenerateDefault(private val rngHelper: RNGHelper): GenerateMainActivities {
    override fun generate(input: PersonWithRoutine, days: List<HDay>): List<ActivityType> {
        return days.map { day ->
            val availableOptions = ActivityType.FULLSET.toMutableSet()
            if(!input.person.isAllowedToWork) availableOptions.remove(ActivityType.WORK)
            step2AWithParams.select(rngHelper.randomValue) {DaySituation(it, input, day)}
        }
    }
}



fun ActitoppPerson.generateMainActivities(weekRoutine: WeekRoutine, lambda: ActitoppPerson.() -> GenerateMainActivities): List<Pair<ActivityType, HDay>> {
    val strategy = this.lambda()
    return strategy.generate(this, weekRoutine, weekPattern.days).zip(weekPattern.days)

}

fun ActitoppPerson.assignMainActivities(activitiesPerDay: List<Pair<ActivityType, HDay>>) {
    activitiesPerDay.forEach { (activityType, day) ->
        val tour = day.generateMainTour()
        tour.generateMainActivity(activityType)
    }
}
