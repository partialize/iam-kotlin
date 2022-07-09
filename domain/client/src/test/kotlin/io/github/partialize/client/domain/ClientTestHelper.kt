package io.github.partialize.client.domain

import io.github.partialize.auth.domain.scope_token.ScopeTokenFactory
import io.github.partialize.auth.domain.scope_token.ScopeTokenMapper
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.migration.CreateScopeRelation
import io.github.partialize.auth.migration.CreateScopeToken
import io.github.partialize.auth.migration.CreateToken
import io.github.partialize.auth.repository.ScopeRelationRepository
import io.github.partialize.auth.repository.ScopeTokenRepository
import io.github.partialize.client.migration.CreateClient
import io.github.partialize.client.migration.CreateClientCredential
import io.github.partialize.client.migration.CreateClientScope
import io.github.partialize.client.repository.ClientCredentialRepository
import io.github.partialize.client.repository.ClientRepository
import io.github.partialize.client.repository.ClientScopeRepository
import io.github.partialize.data.converter.StringToURLConverter
import io.github.partialize.data.converter.URLToStringConverter
import io.github.partialize.data.test.DataTestHelper
import io.github.partialize.data.test.MongoTestHelper
import io.github.partialize.event.EventEmitter
import io.mockk.spyk
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

abstract class ClientTestHelper : DataTestHelper(
    converters = listOf(
        URLToStringConverter(),
        StringToURLConverter()
    )
) {
    init {
        migrationManager
            .register(CreateScopeToken(entityOperations))
            .register(CreateScopeRelation(entityOperations))
            .register(CreateToken(mongoTemplate))
            .register(CreateClient(entityOperations, mongoTemplate))
            .register(CreateClientCredential(entityOperations))
            .register(CreateClientScope(entityOperations))
    }

    protected val eventEmitter = EventEmitter()

    protected val scopeRelationRepository = ScopeRelationRepository(entityOperations)
    protected val scopeTokenRepository = ScopeTokenRepository(entityOperations)
    protected val clientRepository = ClientRepository(entityOperations, eventPublisher = eventEmitter)
    protected val clientCredentialRepository = ClientCredentialRepository(entityOperations, eventPublisher = eventEmitter)
    protected val clientScopeRepository = spyk(ClientScopeRepository(entityOperations, eventPublisher = eventEmitter))

    protected val scopeTokenMapper = ScopeTokenMapper(
        scopeTokenRepository,
        scopeRelationRepository,
        transactionalOperator,
        eventEmitter
    )
    protected val scopeTokenStorage = ScopeTokenStorage(scopeTokenRepository, scopeTokenMapper)
    protected val scopeTokenFactory = ScopeTokenFactory(scopeTokenRepository, scopeTokenMapper, eventEmitter)

    protected val clientMapper = ClientMapper(clientRepository, clientCredentialRepository, clientScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)
    protected val clientsMapper = ClientsMapper(clientRepository, clientCredentialRepository, clientScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)

    protected val clientFactory = ClientFactory(clientRepository, clientCredentialRepository, clientMapper, scopeTokenStorage, transactionalOperator, eventEmitter)
    protected val clientStorage = ClientStorage(clientRepository, clientMapper, clientsMapper)

    @BeforeEach
    override fun setUp() {
        super.setUp()

        blocking {
            scopeTokenFactory.upsert("confidential(client):pack")
            scopeTokenFactory.upsert("public(client):pack")
        }
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
