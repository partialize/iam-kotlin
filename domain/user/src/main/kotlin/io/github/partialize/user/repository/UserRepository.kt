package io.github.partialize.user.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.CacheBuilder
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.r2dbc.R2DBCRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserData
import org.redisson.api.RedissonClient
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.Instant

@Repository
class UserRepository(
    entityOperations: R2dbcEntityOperations,
    objectMapper: ObjectMapper? = null,
    redisClient: RedissonClient? = null,
    eventPublisher: EventPublisher? = null
) : QueryRepository<UserData, ULID> by R2DBCRepositoryBuilder<UserData, ULID>(entityOperations, UserData::class)
    .enableEvent(eventPublisher)
    .enableJsonMapping(objectMapper)
    .enableCache(redisClient, expiredAt = { Instant.now().plus(Duration.ofHours(1)) }, size = 100_000)
    .enableCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(1_000)
    })
    .build()
