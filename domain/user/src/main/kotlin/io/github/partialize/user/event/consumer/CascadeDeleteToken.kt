package io.github.partialize.user.event.consumer

import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.data.criteria.where
import io.github.partialize.data.event.AfterDeleteEvent
import io.github.partialize.data.transaction.doAfterCommit
import io.github.partialize.event.EventConsumer
import io.github.partialize.event.Subscribe
import io.github.partialize.user.entity.UserData
import org.springframework.stereotype.Component

@Component
@Subscribe(filterBy = AfterDeleteEvent::class)
class CascadeDeleteToken(
    private val tokenRepository: TokenRepository
) : EventConsumer<AfterDeleteEvent<*>> {
    override suspend fun consume(event: AfterDeleteEvent<*>) {
        val entity = event.entity as? UserData ?: return

        doAfterCommit {
            tokenRepository.deleteAll(where("claims.uid").`is`(entity.id.toString()))
        }
    }
}
