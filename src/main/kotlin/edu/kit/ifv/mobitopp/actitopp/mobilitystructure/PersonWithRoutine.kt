package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

data class PersonWithRoutine(
    val person: IPerson,
    val routine: WeekRoutine,
) : IPerson by person {


    fun amountOfWorkingDays() = routine.amountOfWorkingDays
    fun amountOfLeisureDays() = routine.amountOfLeisureDays
    fun amountOfEducationDays() = routine.amountOfEducationDays

}