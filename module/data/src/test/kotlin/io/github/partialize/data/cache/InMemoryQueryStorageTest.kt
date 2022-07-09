package io.github.partialize.data.cache

import com.google.common.cache.CacheBuilder
import io.github.partialize.data.entity.Person

class InMemoryQueryStorageTest : QueryStorageTestHelper(InMemoryQueryStorage(Person::class) { CacheBuilder.newBuilder() })
