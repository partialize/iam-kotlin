package io.github.partialize.data.repository.mongo

import io.github.partialize.data.entity.Person

class SimpleMongoRepositoryTest : MongoRepositoryTestHelper(
    repositories = {
        listOf(SimpleMongoRepository(mongoTemplate, Person::class))
    }
)
