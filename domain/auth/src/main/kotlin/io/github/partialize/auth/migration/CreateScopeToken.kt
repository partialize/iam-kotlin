package io.github.partialize.auth.migration

import io.github.partialize.data.migration.Migration
import io.github.partialize.data.migration.createUniqueIndex
import io.github.partialize.data.migration.createUpdatedAtTrigger
import io.github.partialize.data.migration.dropTable
import io.github.partialize.data.migration.fetchSQL
import io.github.partialize.data.migration.isDriver
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

class CreateScopeToken(
    private val entityOperations: R2dbcEntityOperations
) : Migration {
    private val tableName = "scope_tokens"

    override suspend fun up() {
        if (entityOperations.isDriver("PostgreSQL")) {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id BYTEA PRIMARY KEY, " +

                    "name VARCHAR(64) NOT NULL, " +
                    "description VARCHAR(128), " +
                    "system BOOLEAN, " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ")"
            )
            entityOperations.createUpdatedAtTrigger(tableName)
        } else {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id BINARY(16) NOT NULL PRIMARY KEY, " +

                    "name VARCHAR(64) NOT NULL, " +
                    "description VARCHAR(128), " +
                    "system BOOLEAN, " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ")"
            )
        }

        entityOperations.createUniqueIndex(tableName, listOf("name"))
    }

    override suspend fun down() {
        entityOperations.dropTable(tableName)
    }
}
