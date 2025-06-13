package edu.kit.ifv.mobitopp.actitopp.utilityFunctions

class ArrayDiscreteChoiceModel<X: Any, SIT: ChoiceSituation<X>, P>(): SealedDiscreteChoiceModel<X, SIT> {

}



interface IDiscreteChoiceModel<X: Any> {
    fun select()
}
