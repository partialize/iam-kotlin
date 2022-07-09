package io.github.partialize.auth.domain.authentication

import io.github.partialize.auth.domain.Principal
import kotlin.reflect.KClass

interface AuthenticatePipeline<PRINCIPAL : Principal> {
    val clazz: KClass<PRINCIPAL>

    suspend fun pipe(principal: PRINCIPAL): PRINCIPAL
}
