package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authentication.AuthorizationPayload
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
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.Duration

class BearerAuthorizationStrategyTest : UserTestHelper() {
    private val tokenRepository = TokenRepository(mongoTemplate, eventPublisher = eventEmitter)

    private val tokenMapper = TokenMapper(tokenRepository, scopeTokenStorage, eventEmitter)
    private val tokenStorage = TokenStorage(tokenRepository, tokenMapper)

    private val claimEmbedder = ClaimEmbedder()
    private val tokenFactoryProvider = TokenFactoryProvider(claimEmbedder, tokenRepository, tokenMapper)

    private val authorizationStrategy = BearerAuthorizationStrategy(tokenStorage)

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

        assertNull(authorizationStrategy.authenticate(AuthorizationPayload("invalid", token.signature)))
        assertEquals(principal.copy(id = token.id), authorizationStrategy.authenticate(AuthorizationPayload("bearer", token.signature)))
    }
}
