package io.github.partialize.user.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.CacheBuilder
import io.github.partialize.data.criteria.where
import io.github.partialize.data.repository.QueryRepository
import io.github.partialize.data.repository.r2dbc.R2DBCRepositoryBuilder
import io.github.partialize.event.EventPublisher
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserCredentialData
import org.redisson.api.RedissonClient
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.Instant

@Repository
class UserCredentialRepository(
    entityOperations: R2dbcEntityOperations,
    objectMapper: ObjectMapper? = null,
    redisClient: RedissonClient? = null,
    eventPublisher: EventPublisher? = null
) : QueryRepository<UserCredentialData, Long> by R2DBCRepositoryBuilder<UserCredentialData, Long>(entityOperations, UserCredentialData::class)
    .enableEvent(eventPublisher)
    .enableJsonMapping(objectMapper)
    .enableCache(redisClient, expiredAt = { Instant.now().plus(Duration.ofHours(1)) }, size = 100_000)
    .enableCache({
        CacheBuilder.newBuilder()
            .softValues()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(1_000)
    })
    .build() {
    suspend fun findByUserIdOrFail(userId: ULID): UserCredentialData {
        return findByUserId(userId) ?: throw EmptyResultDataAccessException(1)
    }

    suspend fun findByUserId(userId: ULID): UserCredentialData? {
        return findOne(where(UserCredentialData::userId).`is`(userId))
    }

    suspend fun existsByUserId(userId: ULID): Boolean {
        return exists(where(UserCredentialData::userId).`is`(userId))
    }

    suspend fun deleteByUserId(userId: ULID) {
        deleteAll(where(UserCredentialData::userId).`is`(userId))
    }
}
