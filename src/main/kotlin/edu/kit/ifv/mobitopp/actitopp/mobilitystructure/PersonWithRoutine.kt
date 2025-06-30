package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.Person
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

data class PersonWithRoutine(
    val person: Person,
    val routine: WeekRoutine,
) : Person by person {


    fun amountOfWorkingDays() = routine.amountOfWorkingDays
    fun amountOfLeisureDays() = routine.amountOfLeisureDays
    fun amountOfEducationDays() = routine.amountOfEducationDays

}