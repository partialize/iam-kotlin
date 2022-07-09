package io.github.partialize.auth.domain.authentication

import kotlin.reflect.KClass

class TypeMatchAuthenticateFilter<T : Any>(
    private val clazz: KClass<T>
) : AuthenticateFilter {
    override fun isSubscribe(payload: Any): Boolean {
        return clazz.isInstance(payload)
    }
}
