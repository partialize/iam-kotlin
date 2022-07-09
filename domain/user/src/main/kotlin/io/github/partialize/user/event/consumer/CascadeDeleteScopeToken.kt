package io.github.partialize.user.event.consumer

import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.data.event.BeforeDeleteEvent
import io.github.partialize.event.EventConsumer
import io.github.partialize.event.Subscribe
import io.github.partialize.user.repository.UserScopeRepository
import org.springframework.stereotype.Component

@Component
@Subscribe(filterBy = BeforeDeleteEvent::class)
class CascadeDeleteScopeToken(
    private val userScopeRepository: UserScopeRepository
) : EventConsumer<BeforeDeleteEvent<*>> {
    override suspend fun consume(event: BeforeDeleteEvent<*>) {
        val entity = event.entity as? ScopeToken ?: return
        userScopeRepository.deleteAllByScopeTokenId(entity.id)
    }
}
