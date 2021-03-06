package io.github.partialize.auth.domain.scope_token

import io.github.partialize.auth.entity.ScopeTokenData
import io.github.partialize.auth.repository.ScopeTokenRepository
import io.github.partialize.data.event.AfterCreateEvent
import io.github.partialize.event.EventPublisher
import org.springframework.stereotype.Component

@Component
class ScopeTokenFactory(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenMapper: ScopeTokenMapper,
    private val eventPublisher: EventPublisher,
) {
    private val scopeTokenStorage = ScopeTokenStorage(scopeTokenRepository, scopeTokenMapper)

    suspend fun upsert(name: String): ScopeToken {
        return upsert(CreateScopeTokenPayload(name = name))
    }

    suspend fun upsert(payload: CreateScopeTokenPayload): ScopeToken {
        val exited = scopeTokenStorage.load(payload.name)
        return if (exited != null) {
            exited.description = payload.description
            exited.sync()
            exited
        } else {
            create(payload)
        }
    }

    suspend fun create(payload: CreateScopeTokenPayload): ScopeToken {
        return scopeTokenRepository.create(
            ScopeTokenData(
                name = payload.name,
                description = payload.description,
                system = payload.system
            )
        )
            .let { scopeTokenMapper.map(it) }
            .also { eventPublisher.publish(AfterCreateEvent(it)) }
    }
}
