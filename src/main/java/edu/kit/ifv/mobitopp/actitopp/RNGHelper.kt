package edu.kit.ifv.mobitopp.actitopp

import kotlin.math.abs
import kotlin.random.Random

/**
 * @author Tim Hilgert
 */
class RNGHelper private constructor(

    val seed: Long,
    private val rng : Random
) {

    constructor(seed: Long): this(seed = seed, rng = Random(seed))
    /**
     * @return
     */
    @Deprecated("Seems to be only used for debug printing")
    var lastRandomValue: Double = 0.0
        private set

    private var generationCounter = 0
    val randomValue: Double
        /**
         * @return
         */
        get() {
            generationCounter++
            // create randomValue
            val randomvalue = rng.nextDouble()

            // Save for access possibility
            lastRandomValue = randomvalue

            return randomvalue
        }

    /**
     * creates a random key between 0 and bound
     * used to draw a random person out of a list
     *
     * @param bound
     * @return
     */
    fun getRandomPersonKey(bound: Int): Int {
        generationCounter++
        return rng.nextInt(bound)
    }


    /**
     * Nowhere in the codebase is the stepSize set to anything other than 1, so we can kill that procedure.
     * Also random generation can be done without allocating an array and then picking randomly from it
     *
     * If in the future anyone requires stepSize -> In kotlin you can pass the stepSize to the range.
     */
    fun getRandomValueBetween(from: Int, to: Int): Int {
        generationCounter++
        require(from <= to) { "FROM bigger than TO $from $to" }
        return (from..to).random(rng)
    }

    fun copy(): RNGHelper {
        return RNGHelper(seed)
    }

    fun synchronize(other: RNGHelper) {
        val difference = abs(generationCounter - other.generationCounter)
        if(difference == 0) return
        if(generationCounter < other.generationCounter) {
            repeat(difference) {
                this.randomValue
            }
        } else {
            repeat(difference) {
                other.randomValue
            }
        }


    }

    override fun toString(): String {
        return "Rand($seed) $generationCounter"
    }
}



fun RNGHelper.getRandomValues(amount: Int): List<Double> {
    return (0..<amount).map{
        randomValue
    }
}