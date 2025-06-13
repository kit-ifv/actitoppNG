package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.modernization.IHousehold

/**
 * @author Tim Hilgert
 */
class ActiToppHousehold(

    val children0_10: Int,
    val children_u18: Int,
    override val areaType: AreaType,
    val numberofcarsinhousehold: Int = 0,
) : IHousehold {


    override val numberOfCars: Int = numberofcarsinhousehold
    override val id: Int = ID
    override fun amountOfYoungMinors(): Int = children0_10
    override fun amountOfAllMinors() = children_u18


    override val members: MutableList<IPerson> = mutableListOf()
    fun add(person: IPerson) = members.add(person)


    companion object {
        var ID = 0
            get() = field++
            private set
    }
}
