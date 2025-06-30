package edu.kit.ifv.mobitopp.actitopp.steps

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.IHousehold

interface HouseholdAttributes {
    fun areaTypeRural(): Boolean
    fun areaTypeConurbation(): Boolean
    fun hasChildrenInHousehold(): Boolean
    fun amountOfChildrenInHousehold(): Int
    fun hasYouthsInHousehold(): Boolean
    fun amountOfYouthsInHousehold(): Int
    fun amountOfPKW(): Int
}

class HouseholdAttributesFromElement(val household: IHousehold) : HouseholdAttributes {
    override fun areaTypeRural() = household.areaType == AreaType.RURAL
    override fun areaTypeConurbation() = household.areaType == AreaType.CONURBATION
    override fun hasChildrenInHousehold() = household.amountOfYoungMinors() > 0
    override fun amountOfChildrenInHousehold() = household.amountOfYoungMinors()
    override fun hasYouthsInHousehold() = household.amountOfAllMinors() > 0
    override fun amountOfYouthsInHousehold() = household.amountOfAllMinors()
    override fun amountOfPKW(): Int = household.numberOfCars
}