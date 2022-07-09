package io.github.partialize.auth.repository

import com.google.common.cache.CacheBuilder
import io.github.partialize.auth.entity.ScopeRelationData
import io.github.partialize.data.criteria.where
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.r2dbc.R2DBCRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ScopeRelationRepository(
    entityOperations: R2dbcEntityOperations,
    eventPublisher: EventPublisher? = null
) : QueryRepository<ScopeRelationData, Long> by R2DBCRepositoryBuilder<ScopeRelationData, Long>(entityOperations, ScopeRelationData::class)
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

    fun findAllByChildId(childId: ULID): Flow<ScopeRelationData> {
        return findAll(where(ScopeRelationData::childId).`is`(childId))
    }

    fun findAllByParentId(parentIds: Iterable<ULID>): Flow<ScopeRelationData> {
        return findAll(where(ScopeRelationData::parentId).`in`(parentIds.toList()))
    }

    fun findAllByParentId(parentId: ULID): Flow<ScopeRelationData> {
        return findAll(where(ScopeRelationData::parentId).`is`(parentId))
    }

    suspend fun deleteAllByChildId(childId: ULID) {
        return deleteAll(where(ScopeRelationData::childId).`is`(childId))
    }

    suspend fun deleteAllByParentId(parentId: ULID) {
        return deleteAll(where(ScopeRelationData::parentId).`is`(parentId))
    }
}
