package io.github.partialize.user.migration

import com.mongodb.BasicDBObject
import io.github.partialize.data.migration.Migration
import io.github.partialize.data.migration.createUniqueIndex
import io.github.partialize.data.migration.createUpdatedAtTrigger
import io.github.partialize.data.migration.dropTable
import io.github.partialize.data.migration.fetchSQL
import io.github.partialize.data.migration.isDriver
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

class CreateUser(
    private val entityOperations: R2dbcEntityOperations,
    private val mongoTemplate: ReactiveMongoTemplate
) : Migration {
    private val tableName = "users"

    override suspend fun up() {
        if (entityOperations.isDriver("PostgreSQL")) {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id BYTEA PRIMARY KEY, " +

                    "name VARCHAR(64) NOT NULL, " +
                    "email VARCHAR(64) NOT NULL, " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "deleted_at TIMESTAMP" +
                    ")"
            )
            entityOperations.createUpdatedAtTrigger(tableName)
        } else {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id BINARY(16) NOT NULL PRIMARY KEY, " +

                    "name VARCHAR(64) NOT NULL, " +
                    "email VARCHAR(64) NOT NULL, " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                    "deleted_at TIMESTAMP" +
                    ")"
            )
        }

        entityOperations.createUniqueIndex(tableName, listOf("name"))

        mongoTemplate.getCollection("tokens").awaitSingle().apply {
            createIndex(BasicDBObject("claims.uid", 1)).awaitFirstOrNull()
        }
    }

    override suspend fun down() {
        entityOperations.dropTable(tableName)

        mongoTemplate.getCollection("tokens").awaitSingle().apply {
            dropIndex(BasicDBObject("claims.uid", 1)).awaitFirstOrNull()
        }
    }
}
