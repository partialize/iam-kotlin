package io.github.partialize.client.event.consumer

import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.repository.ClientScopeRepository
import io.github.partialize.data.event.BeforeDeleteEvent
import io.github.partialize.event.EventConsumer
import io.github.partialize.event.Subscribe
import org.springframework.stereotype.Component

@Component
@Subscribe(filterBy = BeforeDeleteEvent::class)
class CascadeDeleteScopeToken(
    private val clientScopeRepository: ClientScopeRepository
) : EventConsumer<BeforeDeleteEvent<*>> {
    override suspend fun consume(event: BeforeDeleteEvent<*>) {
        val entity = event.entity as? ScopeToken ?: return
        clientScopeRepository.deleteAllByScopeTokenId(entity.id)
    }
}
