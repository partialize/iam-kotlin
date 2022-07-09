package io.github.partialize.data.repository.r2dbc

import io.github.partialize.data.entity.Person

class SimpleR2DBCRepositoryTest : R2DBCRepositoryTestHelper(
    repositories = { listOf(SimpleR2DBCRepository(EntityManager(it.entityOperations, Person::class))) }
)
