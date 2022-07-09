package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authentication.RefreshTokenPayload
import io.github.partialize.auth.domain.token.ClaimEmbedder
import io.github.partialize.auth.domain.token.TokenFactoryProvider
import io.github.partialize.auth.domain.token.TokenMapper
import io.github.partialize.auth.domain.token.TokenStorage
import io.github.partialize.auth.domain.token.TokenTemplate
import io.github.partialize.auth.domain.token.TypeMatchClaimFilter
import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.user.domain.MockCreateUserPayloadFactory
import io.github.partialize.user.domain.UserTestHelper
import io.github.partialize.user.entity.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration

class RefreshTokenAuthorizationStrategyTest : UserTestHelper() {
    private val tokenRepository = TokenRepository(mongoTemplate, eventPublisher = eventEmitter)

    private val tokenMapper = TokenMapper(tokenRepository, scopeTokenStorage, eventEmitter)
    private val claimEmbedder = ClaimEmbedder()

    private val tokenStorage = TokenStorage(tokenRepository, tokenMapper)
    private val tokenFactoryProvider = TokenFactoryProvider(claimEmbedder, tokenRepository, tokenMapper)

    private val refreshTokenAuthorizationStrategy = RefreshTokenAuthorizationStrategy(tokenStorage)

    init {
        claimEmbedder.register(TypeMatchClaimFilter(UserEntity::class), UserEntityClaimEmbeddingStrategy())
    }

    @Test
    fun authenticate() = blocking {
        val template = TokenTemplate(type = "test")
        val tokenFactory = tokenFactoryProvider.get(template)

        val user = MockCreateUserPayloadFactory.create()
            .let { userFactory.create(it) }
        val principal = user.toPrincipal()

        val token = tokenFactory.create(principal, Duration.ofMinutes(30))

        assertEquals(
            principal.copy(id = token.id),
            refreshTokenAuthorizationStrategy.authenticate(RefreshTokenPayload(token.signature))
        )
    }
}
