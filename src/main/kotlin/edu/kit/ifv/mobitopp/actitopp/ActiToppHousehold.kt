package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.modernization.ModernizedHousehold

/**
 * @author Tim Hilgert
 */
class ActiToppHousehold @JvmOverloads constructor(

    val children0_10: Int,
    val children_u18: Int,
    areatypeCode: Int,
    val numberofcarsinhousehold: Int = 0,
) : ModernizedHousehold{
    val householdIndex: Int = ActiToppHousehold.ID

    val areatype = AreaType.fromCode(areatypeCode)
    override val members: List<IPerson>
        get() = householdmembersasList
    override val areaType: AreaType = areatype
    override val numberOfCars: Int = numberofcarsinhousehold
    override val id: Int = householdIndex
    /**
     * @return the householdIndex
     */


    private val householdmembers: MutableMap<Int, ActitoppPerson> = mutableMapOf()
    
    /**
     * @return the householdmembers
     */
    fun getHouseholdmembersDeprecated(): Map<Int, ActitoppPerson> {
        return householdmembers
    }

    val householdmembersasList: List<ActitoppPerson>
        /**
         * @return the householdmembers
         */
        get() {
            val tmpliste: MutableList<ActitoppPerson> = ArrayList()

            for ((_, value) in getHouseholdmembersDeprecated()) {
                tmpliste.add(value)
            }

            return tmpliste
        }

    /**
     * @param persnrinhousehold
     * @return the person in the household
     */
    fun getHouseholdMember(persnrinhousehold: Int): ActitoppPerson {
        val tmpperson =
            checkNotNull(getHouseholdmembersDeprecated()[persnrinhousehold]) { "Person does not exist in this household!" }
        return tmpperson
    }

    /**
     * @param member
     * @param persnr
     */
    fun addHouseholdmember(member: ActitoppPerson, persnr: Int) {
        assert(householdmembers[persnr] == null) { "Householdmember using this identifier already exists - persnr $persnr" }
        householdmembers[persnr] = member
    }


    val numberofPersonsinHousehold: Int
        /**
         * @return the numberofpersonsinhousehold
         */
        get() = householdmembers.size




    override fun toString(): String {
        val message = StringBuffer()

        message.append("\n household information")

        message.append("\n - HH-index : ")
        message.append(householdIndex)

        message.append("\n - #HH-members : ")
        message.append(numberofPersonsinHousehold)

        message.append("\n - #children 0-10 : ")
        message.append(children0_10)

        message.append("\n - #children <18 : ")
        message.append(children_u18)

        message.append("\n - area type : ")
        message.append(areatype)

        message.append("\n - #car in HH : ")
        message.append(numberofcarsinhousehold)

        return message.toString()
    }




    companion object {
        var ID = 0
            get() = field++
            private set
    }
}
