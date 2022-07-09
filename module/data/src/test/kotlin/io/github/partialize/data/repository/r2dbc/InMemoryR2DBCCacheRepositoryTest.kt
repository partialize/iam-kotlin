package io.github.partialize.data.repository.r2dbc

import com.google.common.cache.CacheBuilder
import io.github.partialize.data.entity.Person
import io.github.partialize.data.repository.TransactionalQueryRepositoryTestHelper
import io.github.partialize.data.repository.r2dbc.migration.CreatePerson
import io.github.partialize.ulid.ULID
import java.time.Duration

class InMemoryR2DBCCacheRepositoryTest : TransactionalQueryRepositoryTestHelper(
    repositories = {
        listOf(
            R2DBCRepositoryBuilder<Person, ULID>(it.entityOperations, Person::class)
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
}
