package edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters

/**
 * THe default implementation tries to generate a week routine for exactly one week, so each choice model parameter set
 * requires to have a parameter configuration for each of these options. Since the choice model uses the option 0
 * as baseline with utility 0.0, that specific amount does not require a parameter option.
 *
 * Each implementing class must provide a field [T] which contains the parameters that should be used for that concrete
 * step.
 */
interface WeekRoutineParameterSet<T>: List<T> {
    val parameters: List<T>
    val option1: T get()= parameters[0]
    val option2: T get()= parameters[1]
    val option3: T get()= parameters[2]
    val option4: T get()= parameters[3]
    val option5: T get()= parameters[4]
    val option6: T get()= parameters[5]
    val option7: T get()= parameters[6]
}