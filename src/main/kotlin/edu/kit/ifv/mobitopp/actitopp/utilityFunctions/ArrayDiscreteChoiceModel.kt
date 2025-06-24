package edu.kit.ifv.mobitopp.actitopp.utilityFunctions




class ArrayDiscreteChoiceModel<X, Y: Any, P> (
    val utilityFunction: P.(X, Y) -> Double,
    val parameters: Map<X, P>,
    val selectionFunction: LimitedDistributionFunction<X>
)

    : ImprovedChoiceModel<X> {
    val output: DoubleArray = DoubleArray(6)
    val keepMap = mutableMapOf<X, Double>()

    lateinit var bonusInformation: Y
    override fun select(randomNumber: Double, options: Set<X>): X {
        options.forEach {
            keepMap[it] = parameters.getValue(it).utilityFunction(it, bonusInformation, )
        }
        val probabilities = selectionFunction.calculateProbabilities(keepMap)
        return probabilities.select(randomNumber)
    }
}

interface ImprovedChoiceModel<X> {

    fun select(randomNumber: Double, options: Set<X>): X

}

//fun main() {
//    val choiceModel = ArrayDiscreteChoiceModel(utilityFunction = { _: Int, it: PersonSituation, ->
//        base +
//                (it.isParttimeEmployee()) * beruf_teilzeit +
//                (it.isStudent()) * beruf_schueler +
//                (it.isVocational()) * beruf_azubi +
//                (it.isAged26To35()) * alter_26bis35 +
//                (it.isAged36To50()) * alter_36bis50 +
//                (it.isAged51To60()) * alter_51bis60 +
//                (it.isAged61To70()) * alter_61bis70 +
//                (it.areaTypeRural()) * Raumtyp_mobitopp_rural +
//                (it.commuteOver50km()) * pendeln_ueber50km +
//                (it.commuteIn0To5km()) * pendeln_0bis5km +
//                (it.hasChildrenInHousehold()) * haushalthatkinderunter10
//    }, parameters = DefaultActivityAmountParameters, Logit<Int, Any>())
//    val household = ActiToppHousehold(
//        children0_10 = 0,
//        children_u18 = 0,
//        areaType = AreaType.CONURBATION,
//        numberOfCars = 0
//    )
//    val person = household.generatePerson()
//    val weekRoutine = WeekRoutineImpl(
//        amountOfWorkingDays = 2,
//        amountOfEducationDays = 2,
//        amountOfLeisureDays = 2,
//        amountOfShoppingDays = 2,
//        amountOfServiceDays = 2,
//        amountOfImmobileDays = 2,
//        averageAmountOfTours = 2,
//        averageAmountOfActivities = 2
//    )
//    choiceModel.bonusInformation = PersonSituation(-999, weekRoutine,  person)
//    for(i in 0..10000000) {
//        val rnd = GlobalRandomizer.nextDouble()
//        val options = setOf(1, 2, 3, 4, 5, 6)
//        val b = choiceModel.select(rnd, options)
//        val converter: (Int) -> PersonSituation = { PersonSituation(it, weekRoutine, person) }
//        val a = step1LWithParams.select(rnd, converter)
//        if(i % 1000 ==0) println(i)
//
//
//    }
//}