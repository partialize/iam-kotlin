package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authentication.AuthenticateStrategy
import io.github.partialize.auth.domain.token.TokenStorage
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.toSet

class TokenAuthorizationStrategy(
    private val tokenStorage: TokenStorage
) : AuthenticateStrategy<String, UserPrincipal> {
    override val clazz = String::class

    override suspend fun authenticate(payload: String): UserPrincipal? {
        val token = tokenStorage.loadOrFail(payload)
        if (token["uid"] == null) {
            return null
        }

        return UserPrincipal(
            id = token.id,
            userId = ULID.fromString(token["uid"].toString()),
            clientId = token["cid"]?.toString()?.let { ULID.fromString(it) },
            scope = token.getScope(deep = true).toSet()
        )
    }
}
