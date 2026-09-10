package edu.kit.ifv.mobitopp.actitoppNG.steps

import edu.kit.ifv.mobitopp.actitoppNG.Household
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType

interface HouseholdAttributes {
    val areaType: AreaType
    val hasChildrenInHousehold: Boolean
    val amountOfChildrenInHousehold: Int
    val hasYouthsInHousehold: Boolean
    val amountOfYouthsInHousehold: Int
    val amountOfPKW: Int
}

class HouseholdAttributesFromElement(val household: Household) : HouseholdAttributes {
    override val areaType = household.areaType
    override val hasChildrenInHousehold = household.amountOfYoungMinors() > 0
    override val amountOfChildrenInHousehold = household.amountOfYoungMinors()
    override val hasYouthsInHousehold = household.amountOfAllMinors() > 0
    override val amountOfYouthsInHousehold = household.amountOfAllMinors()
    override val amountOfPKW: Int = household.numberOfCars
}