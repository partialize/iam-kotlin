package io.github.partialize.data.event

data class AfterDeleteEvent<T>(
    val entity: T
)
