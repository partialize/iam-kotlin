package io.github.partialize.auth.repository

import com.google.common.cache.CacheBuilder
import io.github.partialize.auth.entity.TokenData
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.mongo.MongoRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class TokenRepository(
    template: ReactiveMongoTemplate,
    eventPublisher: EventPublisher? = null
) : QueryRepository<TokenData, ULID> by MongoRepositoryBuilder<TokenData, ULID>(template, TokenData::class)
    .enableEvent(eventPublisher)
    .enableCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(1_000)
    })
    .build()
