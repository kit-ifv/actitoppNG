package edu.kit.ifv.mobitopp.actitopp.utilityFunctions

import edu.kit.ifv.mobitopp.actitopp.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutineImpl
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ActivityAmountParameters
import edu.kit.ifv.mobitopp.generatePerson


class ArrayDiscreteChoiceModel<X, Y: Any, P> (
    val utilityFunction: (X, Y, P) -> Double,
    val parameters: Map<X, P>,
    val selectionFunction: LimitedDistributionFunction<X>
)
    : ImprovedChoiceModel<X> {
    lateinit var bonusInformation: Y
    override fun select(options: Set<X>): X {
        val utilities = options.associateWith { utilityFunction(it, bonusInformation, parameters.getValue(it)) }
        val probabilities = selectionFunction.calculateProbabilities(utilities)
        return probabilities.select(0.5)
    }
}

interface ImprovedChoiceModel<X> {

    fun select(options: Set<X>): X

}

fun main() {
    val choiceModel = ArrayDiscreteChoiceModel(utilityFunction = { _: Int, sit: PersonSituation, params: ActivityAmountParameters ->
        0.0
    }, parameters = emptyMap(), Logit<Int, Any>())
    val household = ActiToppHousehold(
        children0_10 = 0,
        children_u18 = 0,
        areaType = AreaType.CONURBATION,
        numberOfCars = 0
    )
    val person = household.generatePerson()
    val weekRoutine = WeekRoutineImpl(
        amountOfWorkingDays = 2,
        amountOfEducationDays = 2,
        amountOfLeisureDays = 2,
        amountOfShoppingDays = 2,
        amountOfServiceDays = 2,
        amountOfImmobileDays = 2,
        averageAmountOfTours = 2,
        averageAmountOfActivities = 2
    )
    choiceModel.bonusInformation = PersonSituation(-999, weekRoutine,  person)
    println(choiceModel.select(setOf(1, 2, 3, 4, 5, 6, 7)))
}