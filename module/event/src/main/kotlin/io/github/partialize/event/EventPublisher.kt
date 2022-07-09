package io.github.partialize.event

interface EventPublisher {
    suspend fun <E : Any> publish(event: E)
}
