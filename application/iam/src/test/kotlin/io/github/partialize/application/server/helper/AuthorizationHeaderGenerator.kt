package io.github.partialize.application.server.helper

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.domain.scope_token.loadOrFail
import io.github.partialize.auth.domain.token.TokenFactoryProvider
import io.github.partialize.auth.domain.token.TokenTemplate
import io.github.partialize.data.cache.SuspendLazy
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class AuthorizationHeaderGenerator(
    private val scopeTokenStorage: ScopeTokenStorage,
    tokenFactoryProvider: TokenFactoryProvider
) {
    private val accessTokenScope = SuspendLazy {
        scopeTokenStorage.loadOrFail("access-token:create")
    }
    private val refreshTokenScope = SuspendLazy {
        scopeTokenStorage.loadOrFail("refresh-token:create")
    }
    private val accessTokenFactory = tokenFactoryProvider.get(
        TokenTemplate(
            type = "acs",
            limit = listOf(
                "pid" to 1
            )
        )
    )

    suspend fun generate(principal: Principal): String {
        val token = accessTokenFactory.create(
            principal,
            Duration.ofDays(1),
            pop = setOf(accessTokenScope.get(), refreshTokenScope.get())
        )
        return "Bearer ${token.signature}"
    }
}
