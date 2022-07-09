package io.github.partialize.data

fun interface WeekProperty<T : Any, KEY : Any?> {
    fun get(entity: T): KEY
}
