package io.github.partialize.client.event.consumer

import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.client.entity.ClientData
import io.github.partialize.data.criteria.where
import io.github.partialize.data.event.AfterDeleteEvent
import io.github.partialize.data.transaction.doAfterCommit
import io.github.partialize.event.EventConsumer
import io.github.partialize.event.Subscribe
import org.springframework.stereotype.Component

@Component
@Subscribe(filterBy = AfterDeleteEvent::class)
class CascadeDeleteToken(
    private val tokenRepository: TokenRepository
) : EventConsumer<AfterDeleteEvent<*>> {
    override suspend fun consume(event: AfterDeleteEvent<*>) {
        val entity = event.entity as? ClientData ?: return

        doAfterCommit {
            tokenRepository.deleteAll(where("claims.cid").`is`(entity.id.toString()))
        }
    }
}
