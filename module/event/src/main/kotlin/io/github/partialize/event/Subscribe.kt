package io.github.partialize.event

import kotlin.reflect.KClass

annotation class Subscribe(
    val filterBy: KClass<*>
)
