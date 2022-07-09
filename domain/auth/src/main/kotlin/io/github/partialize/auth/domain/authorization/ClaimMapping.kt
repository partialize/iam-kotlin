package io.github.partialize.auth.domain.authorization

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ClaimMapping(
    val filterBy: KClass<*>
)
