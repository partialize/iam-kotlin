package io.github.partialize.client.migration

import io.github.partialize.data.migration.Migration
import io.github.partialize.data.migration.createUniqueIndex
import io.github.partialize.data.migration.createUpdatedAtTrigger
import io.github.partialize.data.migration.dropTable
import io.github.partialize.data.migration.fetchSQL
import io.github.partialize.data.migration.isDriver
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

class CreateClientCredential(
    private val entityOperations: R2dbcEntityOperations
) : Migration {
    private val tableName = "client_credentials"

    override suspend fun up() {
        if (entityOperations.isDriver("PostgreSQL")) {
            entityOperations.fetchSQL(
                "CREATE TABLE $tableName" +
                    "(" +
                    "id SERIAL PRIMARY KEY, " +

                    "client_id BYTEA NOT NULL REFERENCES clients (id), " +

                    "secret VARCHAR(32) NOT NULL, " +

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

                    "client_id BINARY(16) NOT NULL REFERENCES clients (id), " +

                    "secret VARCHAR(32) NOT NULL, " +

                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ")"
            )
        }

        entityOperations.createUniqueIndex(tableName, listOf("client_id"))
    }

    override suspend fun down() {
        entityOperations.dropTable(tableName)
    }
}
