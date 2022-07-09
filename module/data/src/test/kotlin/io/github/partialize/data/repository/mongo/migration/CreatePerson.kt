package io.github.partialize.data.repository.mongo.migration

import io.github.partialize.data.migration.Migration
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class CreatePerson(
    private val mongoTemplate: ReactiveMongoTemplate
) : Migration {
    private val tableName = "persons"

    override suspend fun up() {
        mongoTemplate.createCollection(tableName).awaitSingleOrNull()
    }

    override suspend fun down() {
        mongoTemplate.dropCollection(tableName).awaitSingleOrNull()
    }
}
