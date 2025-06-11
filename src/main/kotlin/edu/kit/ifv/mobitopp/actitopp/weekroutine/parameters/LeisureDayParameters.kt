package edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters



data class LeisureDaySet(
    override val option1: LeisureDayParameters,
    override val option2: LeisureDayParameters,
    override val option3: LeisureDayParameters,
    override val option4: LeisureDayParameters,
    override val option5: LeisureDayParameters,
    override val option6: LeisureDayParameters,
    override val option7: LeisureDayParameters,
): WeekRoutineParameterSet<LeisureDayParameters>

data class LeisureDayParameters(
    val base: Double,
    val employmentIsEarning: Double,
    val emplomentStudent: Double,
    val ageIn61To70: Double,
    val areaTypeIsRural: Double,
    val householdHasChildenBelowAge10: Double,
    val amountOfWorkingDays: Double
)

val DefaultLeisureParameters = LeisureDaySet(
    option1 = LeisureDayParameters(
        base = 0.7714,
        employmentIsEarning = -0.1966,
        emplomentStudent = -0.2525,
        ageIn61To70 = 0.0147,
        areaTypeIsRural = -0.3753,
        householdHasChildenBelowAge10 = 0.2130,
        amountOfWorkingDays = -0.00570,
    ),
    option2 = LeisureDayParameters(
        base = 1.0050,
        employmentIsEarning = -0.0281,
        emplomentStudent = 0.0364,
        ageIn61To70 = 0.1588,
        areaTypeIsRural = -0.4818,
        householdHasChildenBelowAge10 = 0.3640,
        amountOfWorkingDays = -0.0381,
    ),
    option3 = LeisureDayParameters(
        base = 1.3068,
        employmentIsEarning = -0.2120,
        emplomentStudent = -0.0352,
        ageIn61To70 = 0.1633,
        areaTypeIsRural = -0.5532,
        householdHasChildenBelowAge10 = 0.3833,
        amountOfWorkingDays = -0.0663,
    ),
    option4 = LeisureDayParameters(
        base = 1.3841,
        employmentIsEarning = -0.2480,
        emplomentStudent = -0.1466,
        ageIn61To70 = 0.2794,
        areaTypeIsRural = -0.8125,
        householdHasChildenBelowAge10 = 0.3782,
        amountOfWorkingDays = -0.1161,
    ),
    option5 = LeisureDayParameters(
        base = 1.2402,
        employmentIsEarning = -0.3081,
        emplomentStudent = 0.0997,
        ageIn61To70 = 0.3593,
        areaTypeIsRural = -1.0029,
        householdHasChildenBelowAge10 = 0.2191,
        amountOfWorkingDays = -0.1402,
    ),
    option6 = LeisureDayParameters(
        base = 1.0663,
        employmentIsEarning = -0.4165,
        emplomentStudent = 0.0254,
        ageIn61To70 = 0.4184,
        areaTypeIsRural = -1.3017,
        householdHasChildenBelowAge10 = 0.1361,
        amountOfWorkingDays = -0.1811,
    ),
    option7 = LeisureDayParameters(
        base = 0.9369,
        employmentIsEarning = -0.5151,
        emplomentStudent = -0.5478,
        ageIn61To70 = 0.4617,
        areaTypeIsRural = -0.9691,
        householdHasChildenBelowAge10 = -0.0764,
        amountOfWorkingDays = -0.2329,
    )
)