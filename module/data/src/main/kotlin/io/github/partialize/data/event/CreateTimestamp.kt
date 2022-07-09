package io.github.partialize.data.event

import io.github.partialize.data.Modifiable
import io.github.partialize.event.EventConsumer
import java.time.Instant

class CreateTimestamp : EventConsumer<BeforeCreateEvent<*>> {
    override suspend fun consume(event: BeforeCreateEvent<*>) {
        val entity = event.entity

        if (entity !is Modifiable) {
            return
        }

        entity.createdAt = Instant.now()
        entity.updatedAt = Instant.now()
    }
}
