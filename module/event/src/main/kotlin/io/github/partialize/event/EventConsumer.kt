package io.github.partialize.event

interface EventConsumer<E : Any> {
    suspend fun consume(event: E)
}
