package io.github.partialize.application.server.configuration

import io.github.partialize.auth.migration.CreateScopeRelation
import io.github.partialize.auth.migration.CreateScopeToken
import io.github.partialize.auth.migration.CreateToken
import io.github.partialize.client.migration.CreateClient
import io.github.partialize.client.migration.CreateClientCredential
import io.github.partialize.client.migration.CreateClientScope
import io.github.partialize.data.migration.MigrationManager
import io.github.partialize.user.migration.CreateUser
import io.github.partialize.user.migration.CreateUserCredential
import io.github.partialize.user.migration.CreateUserScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.r2dbc.core.R2dbcEntityOperations

@Configuration
class MigrationConfiguration(
    private val entityOperations: R2dbcEntityOperations,
    private val mongoTemplate: ReactiveMongoTemplate
) {
    @Autowired(required = true)
    fun configMigrationManager(migrationManager: MigrationManager) {
        migrationManager
            .register(CreateScopeToken(entityOperations))
            .register(CreateScopeRelation(entityOperations))
            .register(CreateToken(mongoTemplate))
            .register(CreateClient(entityOperations, mongoTemplate))
            .register(CreateClientCredential(entityOperations))
            .register(CreateClientScope(entityOperations))
            .register(CreateUser(entityOperations, mongoTemplate))
            .register(CreateUserCredential(entityOperations))
            .register(CreateUserScope(entityOperations))
    }
}
