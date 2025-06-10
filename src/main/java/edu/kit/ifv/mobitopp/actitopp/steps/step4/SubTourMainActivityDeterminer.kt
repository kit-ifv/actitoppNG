package edu.kit.ifv.mobitopp.actitopp.steps.step4

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.WeekRoutine
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.steps.SubTourInput

//class SubTourMainActivityDeterminer(val rngHelper: RNGHelper) : DetermineSubTourMainActivities {
//    override fun generate(input: SubTourInput): List<ActivityType> {
//
//
//        val tracker = input.tracker
//        val output = tracker.determineActivityTypes(input) { tour ->
//            val day = tour.day
//            val availableOptions = step4WithParams.registeredOptions().toMutableSet()
//            if (!input.person.isAllowedToWork) availableOptions.remove(ActivityType.WORK)
//            if (day.shouldNotBeWork()) availableOptions.remove(ActivityType.WORK)
//            if (day.shouldNotBeEducation()) availableOptions.remove(ActivityType.EDUCATION)
//
//            step4WithParams.select(availableOptions, rngHelper.randomValue) {
//                TourSituation(it, input.person, input.routine, input.day, tour)
//            }
//
//        }
//        return output
//
//
//    }
//}

fun interface DetermineSubTourMainActivities {
    fun generate(input: SubTourInput): List<ActivityType>
    fun generate(person: ActitoppPerson, routine: WeekRoutine, day: HDay) =
        generate(SubTourInput(person, routine, day))
}

