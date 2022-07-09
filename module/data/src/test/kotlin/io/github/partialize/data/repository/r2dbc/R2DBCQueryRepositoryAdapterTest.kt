package io.github.partialize.data.repository.r2dbc

import io.github.partialize.data.entity.Person
import io.github.partialize.data.repository.TransactionalQueryRepositoryTestHelper
import io.github.partialize.data.repository.r2dbc.migration.CreatePerson
import io.github.partialize.ulid.ULID

class R2DBCQueryRepositoryAdapterTest : TransactionalQueryRepositoryTestHelper(
    repositories = {
        listOf(R2DBCRepositoryBuilder<Person, ULID>(it.entityOperations, Person::class).build())
    }
) {
    init {
        migrationManager.register(CreatePerson(entityOperations))
    }
}
