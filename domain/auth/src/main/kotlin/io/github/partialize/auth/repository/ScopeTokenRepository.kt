package io.github.partialize.auth.repository

import com.google.common.cache.CacheBuilder
import io.github.partialize.auth.entity.ScopeTokenData
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.r2dbc.R2DBCRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class ScopeTokenRepository(
    entityOperations: R2dbcEntityOperations,
    eventPublisher: EventPublisher? = null
) : QueryRepository<ScopeTokenData, ULID> by R2DBCRepositoryBuilder<ScopeTokenData, ULID>(entityOperations, ScopeTokenData::class)
    .enableEvent(eventPublisher)
    .enableCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(1_000)
    })
    .build()
