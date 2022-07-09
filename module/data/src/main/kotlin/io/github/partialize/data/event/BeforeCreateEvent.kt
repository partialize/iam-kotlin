package io.github.partialize.data.event

data class BeforeCreateEvent<T>(
    val entity: T
)
