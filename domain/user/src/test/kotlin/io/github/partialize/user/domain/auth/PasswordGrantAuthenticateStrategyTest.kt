package io.github.partialize.user.domain.auth

import io.github.partialize.client.domain.ClientMapper
import io.github.partialize.client.domain.ClientStorage
import io.github.partialize.client.domain.ClientsMapper
import io.github.partialize.client.migration.CreateClient
import io.github.partialize.client.migration.CreateClientCredential
import io.github.partialize.client.migration.CreateClientScope
import io.github.partialize.client.repository.ClientCredentialRepository
import io.github.partialize.client.repository.ClientRepository
import io.github.partialize.client.repository.ClientScopeRepository
import io.github.partialize.data.converter.StringToURLConverter
import io.github.partialize.data.converter.URLToStringConverter
import io.github.partialize.user.domain.MockCreateUserPayloadFactory
import io.github.partialize.user.domain.UserTestHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PasswordGrantAuthenticateStrategyTest : UserTestHelper(
    converters = listOf(
        URLToStringConverter(),
        StringToURLConverter()
    )
) {
    protected val clientRepository = ClientRepository(entityOperations, eventPublisher = eventEmitter)
    protected val clientCredentialRepository = ClientCredentialRepository(entityOperations, eventPublisher = eventEmitter)
    protected val clientScopeRepository = ClientScopeRepository(entityOperations, eventEmitter)

    protected val clientMapper = ClientMapper(clientRepository, clientCredentialRepository, clientScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)
    protected val clientsMapper = ClientsMapper(clientRepository, clientCredentialRepository, clientScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)

    protected val clientStorage = ClientStorage(clientRepository, clientMapper, clientsMapper)

    private val authorizationStrategy = PasswordGrantAuthenticateStrategy(userStorage, clientStorage)

    init {
        migrationManager
            .register(CreateClient(entityOperations, mongoTemplate))
            .register(CreateClientCredential(entityOperations))
            .register(CreateClientScope(entityOperations))
    }

    @Test
    fun authenticate() = blocking {
        val payload = MockCreateUserPayloadFactory.create()
        val user = userFactory.create(payload)
        val principal = user.toPrincipal()

        assertEquals(
            principal,
            authorizationStrategy.authenticate(PasswordGrantPayload(payload.name, payload.password, null))
        )
    }
}
