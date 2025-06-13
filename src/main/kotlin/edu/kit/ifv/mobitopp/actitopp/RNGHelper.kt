package edu.kit.ifv.mobitopp.actitopp

import kotlin.math.abs
import kotlin.random.Random

interface RNGHelper {
    val randomValue: Double

    companion object {
        operator fun invoke(seed: Long) = RNGHelperImpl(seed)
    }
}


class RNGHelperImpl private constructor(

    private val seed: Long,
    private val rng: Random,
) : RNGHelper {

    constructor(seed: Long) : this(seed = seed, rng = Random(seed))



    override val randomValue: Double get() = rng.nextDouble()



    override fun toString(): String {
        return "Rand($seed)"
    }
}



