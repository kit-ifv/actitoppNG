package edu.kit.ifv.mobitopp.actitopp.utils

fun <T, R> Iterator<T>.foldUntil(
    predicate: (T) -> Boolean,
    initial: R,
    operation: (acc: R, T) -> R
): Pair<T?, R> {
    var acc = initial
    while(hasNext()) {
        val current = next()
        if(predicate(current)) {
            return  current to acc
        }
        acc = operation(acc, current)
    }
    return null to acc
}


fun <T> Iterator<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if(predicate(item)) {
            break
        }
    }
    return list
}

fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> {
    return sequence<T> {
        for (item in this@takeUntil) {
            yield(item)
            if(predicate(item)) {
                break
            }
        }
    }

}
fun <T, R> Sequence<T>.foldUntil(
    predicate: (T) -> Boolean,
    initial: R,
    operation: (acc: R, T) -> R
): Pair<
        T?, R> = iterator().foldUntil(predicate, initial, operation)