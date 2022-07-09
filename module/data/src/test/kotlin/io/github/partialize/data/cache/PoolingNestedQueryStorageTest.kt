package io.github.partialize.data.cache

import com.google.common.cache.CacheBuilder
import io.github.partialize.data.entity.Person

class PoolingNestedQueryStorageTest : NestedQueryStorageTestHelper(
    PoolingNestedQueryStorage(Pool { InMemoryQueryStorage(Person::class) { CacheBuilder.newBuilder() } })
)
