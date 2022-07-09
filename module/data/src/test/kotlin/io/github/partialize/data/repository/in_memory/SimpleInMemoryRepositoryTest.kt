package io.github.partialize.data.repository.in_memory

import io.github.partialize.data.entity.Person
import io.github.partialize.data.repository.RepositoryTestHelper
import io.github.partialize.ulid.ULID

class SimpleInMemoryRepositoryTest : RepositoryTestHelper<InMemoryRepository<Person, ULID>>(
    repositories = {
        listOf(
            SimpleInMemoryRepository(Person::class)
        )
    }
)
