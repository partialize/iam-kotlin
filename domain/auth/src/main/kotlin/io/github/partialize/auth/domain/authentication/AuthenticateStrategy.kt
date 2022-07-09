package io.github.partialize.auth.domain.authentication

import io.github.partialize.auth.domain.Principal
import kotlin.reflect.KClass

interface AuthenticateStrategy<PAYLOAD : Any, PRINCIPAL : Principal> {
    val clazz: KClass<PAYLOAD>

    suspend fun authenticate(payload: PAYLOAD): PRINCIPAL?
}
