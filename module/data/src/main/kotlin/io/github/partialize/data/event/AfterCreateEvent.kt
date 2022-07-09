package io.github.partialize.data.event

data class AfterCreateEvent<T>(
    val entity: T
)
