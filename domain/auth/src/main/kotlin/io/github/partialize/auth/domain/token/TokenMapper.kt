package io.github.partialize.auth.domain.token

import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.entity.TokenData
import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.event.EventPublisher
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.TypeReference
import org.springframework.stereotype.Component

@Component
class TokenMapper(
    private val tokenRepository: TokenRepository,
    private val scopeTokenStorage: ScopeTokenStorage,
    private val eventPublisher: EventPublisher
) : Mapper<TokenData, Token> {
    override val sourceType = object : TypeReference<TokenData>() {}
    override val targetType = object : TypeReference<Token>() {}

    override suspend fun map(source: TokenData): Token {
        return Token(
            source,
            tokenRepository,
            scopeTokenStorage,
            eventPublisher
        )
    }
}
