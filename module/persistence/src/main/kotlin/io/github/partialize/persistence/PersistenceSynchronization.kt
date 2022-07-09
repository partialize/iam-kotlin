package io.github.partialize.persistence

interface PersistenceSynchronization {
    suspend fun beforeClear() {}
    suspend fun afterClear() {}

    suspend fun beforeSync() {}
    suspend fun afterSync() {}
}
