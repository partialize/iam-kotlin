package io.github.partialize.user.migration

import io.github.partialize.data.migration.Migration
import io.github.partialize.data.migration.createIndex
import io.github.partialize.data.migration.createUniqueIndex
import io.github.partialize.data.migration.createUpdatedAtTrigger
import io.github.partialize.data.migration.dropTable
import io.github.partialize.data.migration.fetchSQL
import io.github.partialize.data.migration.isDriver
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

class CreateUserScope(
    private val entityOperations: R2dbcEntityOperations
) : Migration {
    private val tableName = "user_scopes"

    override suspend fun up() {
        if (entityOperations.isDriver("PostgreSQL")) {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id SERIAL PRIMARY KEY, " +

                    "user_id BYTEA NOT NULL REFERENCES users (id), " +
                    "scope_token_id BYTEA NOT NULL REFERENCES scope_tokens (id), " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ")"
            )
            entityOperations.createUpdatedAtTrigger(tableName)
        } else {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +

                    "user_id BINARY(16) NOT NULL REFERENCES users (id), " +
                    "scope_token_id BINARY(16) NOT NULL REFERENCES scope_tokens (id), " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ")"
            )
        }

        entityOperations.createUniqueIndex(tableName, listOf("user_id", "scope_token_id"))
        entityOperations.createIndex(tableName, listOf("user_id"))
        entityOperations.createIndex(tableName, listOf("scope_token_id"))
    }

    override suspend fun down() {
        entityOperations.dropTable(tableName)
    }
}
