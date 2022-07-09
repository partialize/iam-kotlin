package io.github.partialize.data.cache

import com.google.common.cache.CacheBuilder
import io.github.partialize.data.WeekProperty
import io.github.partialize.data.entity.Person
import io.github.partialize.ulid.ULID

class PoolingNestedStorageTest : NestedStorageTestHelper(
    run {
        val idProperty = object : WeekProperty<Person, ULID?> {
            override fun get(entity: Person): ULID {
                return entity.id
            }
        }
        PoolingNestedStorage(
            Pool {
                InMemoryStorage(
                    { CacheBuilder.newBuilder() },
                    idProperty
                )
            },
            idProperty
        )
    }
)
