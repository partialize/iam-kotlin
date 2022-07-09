package io.github.partialize.user.repository

import com.google.common.cache.CacheBuilder
import io.github.partialize.data.criteria.where
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.r2dbc.R2DBCRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserScopeData
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class UserScopeRepository(
    entityOperations: R2dbcEntityOperations,
    eventPublisher: EventPublisher? = null
) : QueryRepository<UserScopeData, Long> by R2DBCRepositoryBuilder<UserScopeData, Long>(entityOperations, UserScopeData::class)
    .enableEvent(eventPublisher)
    .enableCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(1_000)
    })
    .enableQueryCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofSeconds(1))
            .maximumSize(1_000)
    })
    .build() {
    fun findAllByUserId(userId: ULID): Flow<UserScopeData> {
        return findAll(where(UserScopeData::userId).`is`(userId))
    }

    fun findAllByScopeTokenId(scopeTokenId: ULID): Flow<UserScopeData> {
        return findAll(where(UserScopeData::scopeTokenId).`is`(scopeTokenId))
    }

    suspend fun deleteAllByUserId(userId: ULID) {
        deleteAll(where(UserScopeData::userId).`is`(userId))
    }

    suspend fun deleteAllByScopeTokenId(scopeTokenId: ULID) {
        deleteAll(where(UserScopeData::scopeTokenId).`is`(scopeTokenId))
    }
}
