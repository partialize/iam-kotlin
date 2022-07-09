package io.github.partialize.auth.domain.scope_token

import io.github.partialize.auth.entity.ScopeTokenData
import io.github.partialize.auth.repository.ScopeRelationRepository
import io.github.partialize.auth.repository.ScopeTokenRepository
import io.github.partialize.event.EventPublisher
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.TypeReference
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class ScopeTokenMapper(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeRelationRepository: ScopeRelationRepository,
    private val operator: TransactionalOperator,
    private val eventPublisher: EventPublisher,
) : Mapper<ScopeTokenData, ScopeToken> {
    override val sourceType = object : TypeReference<ScopeTokenData>() {}
    override val targetType = object : TypeReference<ScopeToken>() {}

    override suspend fun map(source: ScopeTokenData): ScopeToken {
        return ScopeToken(
            source,
            scopeTokenRepository,
            scopeRelationRepository,
            operator,
            eventPublisher
        )
    }
}
