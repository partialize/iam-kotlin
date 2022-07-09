package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authentication.AuthenticateMapping
import io.github.partialize.auth.domain.authentication.AuthenticateStrategy
import io.github.partialize.auth.domain.authentication.RefreshTokenPayload
import io.github.partialize.auth.domain.token.TokenStorage
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@AuthenticateMapping
@Order(Ordered.LOWEST_PRECEDENCE - 1)
class RefreshTokenAuthorizationStrategy(
    tokenStorage: TokenStorage
) : AuthenticateStrategy<RefreshTokenPayload, UserPrincipal> {
    override val clazz = RefreshTokenPayload::class

    private val tokenAuthorizationStrategy = TokenAuthorizationStrategy(tokenStorage)

    override suspend fun authenticate(payload: RefreshTokenPayload): UserPrincipal? {
        return tokenAuthorizationStrategy.authenticate(payload.refreshToken)
    }
}
