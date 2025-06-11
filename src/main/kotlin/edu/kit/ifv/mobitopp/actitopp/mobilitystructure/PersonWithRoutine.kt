package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

data class PersonWithRoutine(
    val person: IPerson,
    val routine: WeekRoutine,
): IPerson by person{


    fun amountOfWorkingDays() = routine.amountOfWorkingDays
    fun amountOfLeisureDays() = routine.amountOfLeisureDays
    fun amountOfEducationDays() = routine.amountOfEducationDays
    fun amountOfShoppingDays() = routine.amountOfShoppingDays
    fun amountOfServiceDays() = routine.amountOfServiceDays
    fun amountOfImmobileDays() = routine.amountOfImmobileDays
    fun has5WorkDays() = routine.amountOfWorkingDays == 5
    fun has5EducationDays() = routine.amountOfEducationDays == 5

}