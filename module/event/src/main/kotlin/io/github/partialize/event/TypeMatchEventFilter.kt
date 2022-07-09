package io.github.partialize.event

import kotlin.reflect.KClass

class TypeMatchEventFilter<T : Any>(
    private val clazz: KClass<T>
) : EventFilter {
    override suspend fun <E : Any> filter(event: E): Boolean {
        return clazz.isInstance(event)
    }
}
