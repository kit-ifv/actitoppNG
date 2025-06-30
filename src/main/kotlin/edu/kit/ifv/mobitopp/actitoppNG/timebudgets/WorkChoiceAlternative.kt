package edu.kit.ifv.mobitopp.actitoppNG.timebudgets

import discreteChoice.models.ChoiceAlternative
import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAttributesFromElement

class WorkChoiceAlternative private constructor(
    override val choice: ArrayHistogram,
    val personAttributes: PersonAttributes,
    val patternAttributes: FinalizedPatternAttributes,
) :
    ChoiceAlternative<ArrayHistogram>(), PersonAttributes by personAttributes,
    FinalizedPatternAttributes by patternAttributes {
    constructor(choice: ArrayHistogram, finalizedPattern: FinalizedActivityPattern, person: Person) : this(
        choice,
        PersonAttributesFromElement(person),
        PatternAttributesByElement(finalizedPattern)
    )

}