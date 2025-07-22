package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitoppNG.ActitoppPerson
import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.PersonAttributes
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.Gender
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.ModifiableWeekRoutine
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultWorkParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.WorkDayParameters
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WorkDayTest {

    private val parameterSet = DefaultWorkParameters
    @Test
    fun baseCase() {
        val person = createCharacteristics{
            employment = Employment.DEFINITELY_UNKNOWN
            age = 99
            areaType = AreaType.UNKNOWN
            amountOfChildren = 0
            gender = Gender.FEMALE
        }
        person.testUtilityEquality { base }

    }
    @Test
    fun gender() {
        val person = createCharacteristics{
            employment = Employment.DEFINITELY_UNKNOWN
            age = 99
            areaType = AreaType.UNKNOWN
            amountOfChildren = 0
            gender = Gender.MALE
        }
        person.testUtilityEquality { base + genderIsMale }

    }
    @Test
    fun employment() {
        val person = createCharacteristics{
            employment = Employment.FULLTIME
            age = 99
            areaType = AreaType.UNKNOWN
            amountOfChildren = 0
            gender = Gender.FEMALE
        }
        person.testUtilityEquality { base + employmentFullTime }
    }
    private fun Person.testUtilityEquality(evaluator: WorkDayParameters.() -> Double) {
        val expected = expectedUtility(evaluator)
        context(PersonAlternative(ModifiableWeekRoutine(), this)) {
            assertEquals(expected, defaultWorkDayChoiceModel.utilities())
        }
    }
    private fun expectedUtility(evaluator: WorkDayParameters.() -> Double): Map<Int, Double> {
        return parameterSet.entries.associate { (k, v) -> k to v.evaluator() } + mapOf(0 to 0.0)
    }
    private fun createCharacteristics(lambda: TestBuilder.()-> Unit): Person {
        val builder = TestBuilder()
        builder.apply(lambda)
        return builder.build()
    }
}

private class TestBuilder(

) {

    var employment: Employment = Employment.DEFINITELY_UNKNOWN
    var age: Int = 0
    var areaType: AreaType = AreaType.UNKNOWN
    var gender: Gender = Gender.UNKNOWN
    var amountOfChildren: Int = 0
    fun build(): Person {
        val hh = ActiToppHousehold(
            amountOfChildren,
            0,
            areaType,
            0
        )
        val personAttributes = PersonAttributes(
            gender = gender,
            employment = employment,
            age = age
        )
        return ActitoppPerson(hh, personAttributes)

    }
}
