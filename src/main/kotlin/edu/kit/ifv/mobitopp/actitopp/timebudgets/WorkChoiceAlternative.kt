package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.models.ChoiceAlternative
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributesFromElement

class WorkChoiceAlternative private constructor(
    override val choice: ArrayHistogram,
    val personAttributes: PersonAttributes,
    val patternAttributes: FinalizedPatternAttributes,
) :
    ChoiceAlternative<ArrayHistogram>(), PersonAttributes by personAttributes,
    FinalizedPatternAttributes by patternAttributes {
    constructor(choice: ArrayHistogram, finalizedPattern: FinalizedActivityPattern, person: IPerson) : this(
        choice,
        PersonAttributesFromElement(person),
        PatternAttributesByElement(finalizedPattern)
    )

}