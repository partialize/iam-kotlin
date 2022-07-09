package io.github.partialize.event

interface EventFilter {
    suspend fun <E : Any> filter(event: E): Boolean
}
