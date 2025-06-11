package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine


fun interface GenerateMainActivities {
    fun generate(input: PersonWithRoutine, days: List<HDay>): List<ActivityType>

    fun generate(person: ActitoppPerson, routine: WeekRoutine, days: List<HDay>) = generate(PersonWithRoutine(person, routine), days)
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
