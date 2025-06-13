package edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters


data class ActivityAmountSet(
    val optionList: List<ActivityAmountParameters>,


    ) : Map<Int, ActivityAmountParameters> {

    val option2: ActivityAmountParameters = optionList[0]
    val option3: ActivityAmountParameters = optionList[1]
    val option4: ActivityAmountParameters = optionList[2]
    val option5: ActivityAmountParameters = optionList[3]
    val option6: ActivityAmountParameters = optionList[4]

    override operator fun get(index: Int): ActivityAmountParameters {
        return optionList.getOrNull(index - 2)?: ActivityAmountParameters.EMPTY
    }

    companion object {
        operator fun invoke(
            option2: ActivityAmountParameters,
            option3: ActivityAmountParameters,
            option4: ActivityAmountParameters,
            option5: ActivityAmountParameters,
            option6: ActivityAmountParameters,
        ): ActivityAmountSet {
            return ActivityAmountSet(
                listOf(option2, option3, option4, option5, option6)
            )
        }
    }

    override val size: Int = optionList.size

    /**
     * Returns a read-only [Set] of all key/value pairs in this map.
     */
    override val entries: Set<Map.Entry<Int, ActivityAmountParameters>>
        get() = TODO("Not yet implemented")

    /**
     * Returns a read-only [Set] of all keys in this map.
     */
    override val keys: Set<Int> = optionList.indices.toSet()

    /**
     * Returns a read-only [Collection] of all values in this map. Note that this collection may contain duplicate values.
     */
    override val values: Collection<ActivityAmountParameters> = optionList

    /**
     * Returns `true` if the map is empty (contains no elements), `false` otherwise.
     */
    override fun isEmpty(): Boolean {
        return optionList.isEmpty()
    }

    /**
     * Returns `true` if the map maps one or more keys to the specified [value].
     */
    override fun containsValue(value: ActivityAmountParameters): Boolean {
        return optionList.contains(value)
    }

    /**
     * Returns `true` if the map contains the specified [key].
     */
    override fun containsKey(key: Int): Boolean {
        return key in optionList.indices
    }
}

data class ActivityAmountParameters(
    val base: Double,
    val beruf_teilzeit: Double,
    val beruf_schueler: Double,
    val beruf_azubi: Double,
    val alter_26bis35: Double,
    val alter_36bis50: Double,
    val alter_51bis60: Double,
    val alter_61bis70: Double,
    val Raumtyp_mobitopp_rural: Double,
    val pendeln_ueber50km: Double,
    val pendeln_0bis5km: Double,
    val haushalthatkinderunter10: Double,

    ) {
    companion object {
        val EMPTY = ActivityAmountParameters(0.0,
            0.0, 0.0, 0.0,
            0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0)
    }
}

val DefaultActivityAmountParameters = ActivityAmountSet(
    option2 = ActivityAmountParameters(
        base = 0.3956,
        beruf_teilzeit = 0.4052,
        beruf_schueler = 0.3082,
        beruf_azubi = 0.2121,
        alter_26bis35 = 0.4286,
        alter_36bis50 = 0.5350,
        alter_51bis60 = 0.3188,
        alter_61bis70 = 0.3318,
        Raumtyp_mobitopp_rural = -0.5865,
        pendeln_ueber50km = -0.1666,
        pendeln_0bis5km = 0.2628,
        haushalthatkinderunter10 = 0.1804,
    ),
    option3 = ActivityAmountParameters(
        base = -0.9043,
        beruf_teilzeit = 0.6790,
        beruf_schueler = 0.2746,
        beruf_azubi = 0.1893,
        alter_26bis35 = 1.1473,
        alter_36bis50 = 1.1567,
        alter_51bis60 = 0.6968,
        alter_61bis70 = 0.6427,
        Raumtyp_mobitopp_rural = -0.6093,
        pendeln_ueber50km = -0.4316,
        pendeln_0bis5km = 0.3914,
        haushalthatkinderunter10 = 0.5053,
    ),
    option4 = ActivityAmountParameters(
        base = -2.5651,
        beruf_teilzeit = 0.8759,
        beruf_schueler = 0.2732,
        beruf_azubi = 0.9468,
        alter_26bis35 = 1.6813,
        alter_36bis50 = 1.7647,
        alter_51bis60 = 1.1197,
        alter_61bis70 = 0.8423,
        Raumtyp_mobitopp_rural = -1.0050,
        pendeln_ueber50km = -0.4089,
        pendeln_0bis5km = 0.4109,
        haushalthatkinderunter10 = 0.6865,
    ),
    option5 = ActivityAmountParameters(
        base = -3.7358,
        beruf_teilzeit = 0.9369,
        beruf_schueler = 0.3443,
        beruf_azubi = 0.5735,
        alter_26bis35 = 1.8968,
        alter_36bis50 = 1.7846,
        alter_51bis60 = 1.0495,
        alter_61bis70 = 0.5129,
        Raumtyp_mobitopp_rural = -0.3769,
        pendeln_ueber50km = -0.3099,
        pendeln_0bis5km = 0.2829,
        haushalthatkinderunter10 = 0.6870,
    ),
    option6 = ActivityAmountParameters(
        base = -4.6359,
        beruf_teilzeit = 0.9776,
        beruf_schueler = 0.5584,
        beruf_azubi = 0.3049,
        alter_26bis35 = 2.0686,
        alter_36bis50 = 1.8213,
        alter_51bis60 = 1.4204,
        alter_61bis70 = 0.4929,
        Raumtyp_mobitopp_rural = -0.0415,
        pendeln_ueber50km = -0.1946,
        pendeln_0bis5km = 0.2037,
        haushalthatkinderunter10 = 0.7003,
    ),
)