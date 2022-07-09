package io.github.partialize.client.event.consumer

import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.domain.ClientStorage
import io.github.partialize.client.property.RootClientProperty
import io.github.partialize.data.event.AfterCreateEvent
import io.github.partialize.event.EventConsumer
import io.github.partialize.event.Subscribe
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component

@Component
@Subscribe(filterBy = AfterCreateEvent::class)
class SyncRootClientScope(
    private val clientStorage: ClientStorage,
    private val property: RootClientProperty,
) : EventConsumer<AfterCreateEvent<*>> {
    override suspend fun consume(event: AfterCreateEvent<*>) {
        val entity = event.entity as? ScopeToken ?: return
        val client = clientStorage.load(property.name) ?: return

        try {
            client.grant(entity)
        } catch (_: DataIntegrityViolationException) {
        }
    }
}
