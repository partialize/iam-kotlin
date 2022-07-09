package io.github.partialize.data.repository.mongo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.cache.CacheBuilder
import io.github.partialize.data.entity.Person
import io.github.partialize.data.jackson.instant.InstantEpochTimeModule
import io.github.partialize.data.repository.QueryRepositoryTestHelper
import io.github.partialize.data.repository.mongo.migration.CreatePerson
import io.github.partialize.data.test.MongoTestHelper
import io.github.partialize.data.test.RedisTestHelper
import io.github.partialize.ulid.ULID
import io.github.partialize.ulid.jackson.ULIDModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.redisson.api.RedissonClient
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import java.time.Duration
import java.time.Instant

class RedisCacheRepositoryTest : QueryRepositoryTestHelper(
    repositories = {
        listOf(
            MongoRepositoryBuilder<Person, ULID>(mongoTemplate, Person::class)
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
        migrationManager.register(CreatePerson(mongoTemplate))
    }

    companion object {
        private val redisHelper = RedisTestHelper()
        private val mongoHelper = MongoTestHelper()

        val redisClient: RedissonClient
            get() = redisHelper.redisClient
        val mongoTemplate: ReactiveMongoTemplate
            get() = mongoHelper.mongoTemplate

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            redisHelper.setUp()
            mongoHelper.setUp()
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            redisHelper.tearDown()
            mongoHelper.tearDown()
        }
    }
}
