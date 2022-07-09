package io.github.partialize.auth.domain.token

import io.github.partialize.auth.repository.TokenRepository
import org.springframework.stereotype.Component

@Component
class TokenFactoryProvider(
    private val claimEmbedder: ClaimEmbedder,
    private val tokenRepository: TokenRepository,
    private val tokenMapper: TokenMapper,
) {
    fun get(tokenTemplate: TokenTemplate): TokenFactory {
        return TokenFactory(
            tokenTemplate,
            claimEmbedder,
            tokenRepository,
            tokenMapper
        )
    }
}
