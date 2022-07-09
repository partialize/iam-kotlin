package io.github.partialize.auth.domain

import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Component
annotation class PrincipalMapping(
    val clazz: KClass<out Principal>,
)
