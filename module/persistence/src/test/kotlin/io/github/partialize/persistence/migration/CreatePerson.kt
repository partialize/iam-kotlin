package io.github.partialize.persistence.migration

import io.github.partialize.data.migration.Migration
import io.github.partialize.data.migration.createUniqueIndex
import io.github.partialize.data.migration.dropTable
import io.github.partialize.data.migration.fetchSQL
import io.github.partialize.data.migration.isExistTable
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

class CreatePerson(
    private val entityOperations: R2dbcEntityOperations
) : Migration {
    private val tableName = "persons"

    override suspend fun up() {
        if (entityOperations.isExistTable(tableName)) {
            return
        }
        entityOperations.fetchSQL(
            "CREATE TABLE $tableName" +
                "(" +
                "id BINARY(16) NOT NULL PRIMARY KEY, " +

                "name VARCHAR(64) NOT NULL, " +
                "age INT NOT NULL, " +

                "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")"
        )

        entityOperations.createUniqueIndex(tableName, listOf("name"))
    }

    override suspend fun down() {
        if (!entityOperations.isExistTable(tableName)) {
            return
        }
        entityOperations.dropTable(tableName)
    }
}
