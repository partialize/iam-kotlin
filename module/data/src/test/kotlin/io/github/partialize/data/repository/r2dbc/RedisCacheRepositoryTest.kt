package io.github.partialize.data.repository.r2dbc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.cache.CacheBuilder
import io.github.partialize.data.entity.Person
import io.github.partialize.data.jackson.instant.InstantEpochTimeModule
import io.github.partialize.data.repository.TransactionalQueryRepositoryTestHelper
import io.github.partialize.data.repository.r2dbc.migration.CreatePerson
import io.github.partialize.data.test.RedisTestHelper
import io.github.partialize.ulid.ULID
import io.github.partialize.ulid.jackson.ULIDModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.redisson.api.RedissonClient
import java.time.Duration
import java.time.Instant

class RedisCacheRepositoryTest : TransactionalQueryRepositoryTestHelper(
    repositories = {
        listOf(
            R2DBCRepositoryBuilder<Person, ULID>(it.entityOperations, Person::class)
                .enableJsonMapping(
                    jacksonObjectMapper().apply {
                        registerModule(ULIDModule())
                        registerModule(InstantEpochTimeModule())
                    }
                )
                .enableCache(redisClient, expiredAt = { Instant.now().plus(Duration.ofMinutes(30)) }, size = 1000)
                .enableCache {
                    CacheBuilder.newBuilder()
                        .softValues()
                        .expireAfterAccess(Duration.ofMinutes(2))
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .maximumSize(1_000)
                }.build()
        )
    }
) {
    init {
        migrationManager.register(CreatePerson(entityOperations))
    }

    companion object {
        private val helper = RedisTestHelper()

        val redisClient: RedissonClient
            get() = helper.redisClient

        @BeforeAll
        @JvmStatic
        fun setUpAll() = helper.setUp()

        @AfterAll
        @JvmStatic
        fun tearDownAll() = helper.tearDown()
    }
}
