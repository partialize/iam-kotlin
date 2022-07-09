package io.github.partialize.data.event

data class BeforeDeleteEvent<T>(
    val entity: T
)
