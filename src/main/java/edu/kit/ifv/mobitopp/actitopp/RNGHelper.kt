package edu.kit.ifv.mobitopp.actitopp

import java.util.Queue
import kotlin.math.abs
import kotlin.random.Random

interface RNGHelper {
    val randomValue: Double
    val generationCounter: Int
    fun copy(): RNGHelper
    @Deprecated("Pure int index based should never occur, use a wrapper")
    fun getRandomPersonKey(size: Int): Int
    fun getRandomValueBetween(from: Int, to: Int): Int
    companion object {
        operator fun invoke(seed: Long) = RNGHelperImpl(seed)
    }
}

/**
 * @author Tim Hilgert
 */
class RNGHelperImpl private constructor(

    val seed: Long,
    private val rng : Random
): RNGHelper {

    constructor(seed: Long): this(seed = seed, rng = Random(seed))
    /**
     * @return
     */
    @Deprecated("Seems to be only used for debug printing")
    var lastRandomValue: Double = 0.0
        private set

    override var generationCounter = 0
    override val randomValue: Double
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

    override fun getRandomPersonKey(bound: Int): Int {
        generationCounter++
        return rng.nextInt(bound)
    }


    /**
     * Nowhere in the codebase is the stepSize set to anything other than 1, so we can kill that procedure.
     * Also random generation can be done without allocating an array and then picking randomly from it
     *
     * If in the future anyone requires stepSize -> In kotlin you can pass the stepSize to the range.
     */
    override fun getRandomValueBetween(from: Int, to: Int): Int {
        generationCounter++
        require(from <= to) { "FROM bigger than TO $from $to" }
        return (from..to).random(rng)
    }

    override fun copy(): RNGHelper {
        return RNGHelperImpl(seed)
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

    fun isSynchronized(other: RNGHelper) = generationCounter == other.generationCounter

    override fun toString(): String {
        return "Rand($seed) $generationCounter"
    }
}



fun RNGHelper.getRandomValues(amount: Int): List<Double> {
    return (0..<amount).map{
        randomValue
    }
}

/**
 * The randomness of legacy actitopp is stepped through in a nondecipherable way, so we track the randomness generated,
 * and pull the values if needed.
 */
class RNGKeeper(val original: RNGHelper): RNGHelper by original {
    val tracker: MutableMap<String, ArrayDeque<Double>> = mutableMapOf()
    fun generate(id: String): Double {
        val rnd = original.randomValue
        val queue = tracker.getOrPut(id) {
            ArrayDeque()
        }
        queue.add(rnd)
        return rnd
    }

    fun pull(id: String): Double {

        val queue = tracker.getOrElse(id) {
            throw NoSuchElementException("This shouldn't work$id does not exist ${tracker.keys}")
        }
        if(queue.isEmpty()) {
            throw NoSuchElementException("Queue should not be empty")
        }
        return queue.removeFirst()
    }

}