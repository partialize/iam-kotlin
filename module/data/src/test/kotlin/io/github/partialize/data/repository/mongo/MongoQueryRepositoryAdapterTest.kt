package io.github.partialize.data.repository.mongo

import io.github.partialize.data.entity.Person
import io.github.partialize.data.repository.QueryRepositoryTestHelper
import io.github.partialize.data.repository.mongo.migration.CreatePerson
import io.github.partialize.data.test.MongoTestHelper
import io.github.partialize.ulid.ULID
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class MongoQueryRepositoryAdapterTest : QueryRepositoryTestHelper(
    repositories = {
        listOf(MongoRepositoryBuilder<Person, ULID>(mongoTemplate, Person::class).build())
    }
) {
    init {
        migrationManager.register(CreatePerson(mongoTemplate))
    }

    companion object {
        private val helper = MongoTestHelper()

        val mongoTemplate: ReactiveMongoTemplate
            get() = helper.mongoTemplate

        @BeforeAll
        @JvmStatic
        fun setUpAll() = helper.setUp()

        @AfterAll
        @JvmStatic
        fun tearDownAll() = helper.tearDown()
    }
}
