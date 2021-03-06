package io.github.partialize.user.domain

import io.github.partialize.auth.domain.scope_token.ScopeTokenFactory
import io.github.partialize.auth.domain.scope_token.ScopeTokenMapper
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.migration.CreateScopeRelation
import io.github.partialize.auth.migration.CreateScopeToken
import io.github.partialize.auth.migration.CreateToken
import io.github.partialize.auth.repository.ScopeRelationRepository
import io.github.partialize.auth.repository.ScopeTokenRepository
import io.github.partialize.data.test.DataTestHelper
import io.github.partialize.data.test.MongoTestHelper
import io.github.partialize.event.EventEmitter
import io.github.partialize.user.migration.CreateUser
import io.github.partialize.user.migration.CreateUserCredential
import io.github.partialize.user.migration.CreateUserScope
import io.github.partialize.user.repository.UserCredentialRepository
import io.github.partialize.user.repository.UserRepository
import io.github.partialize.user.repository.UserScopeRepository
import io.mockk.spyk
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

abstract class UserTestHelper(
    converters: Collection<Converter<*, *>> = emptyList()
) : DataTestHelper(converters) {
    init {
        migrationManager
            .register(CreateScopeToken(entityOperations))
            .register(CreateScopeRelation(entityOperations))
            .register(CreateToken(mongoTemplate))
            .register(CreateUser(entityOperations, mongoTemplate))
            .register(CreateUserCredential(entityOperations))
            .register(CreateUserScope(entityOperations))
    }

    protected val eventEmitter = EventEmitter()

    protected val scopeRelationRepository = ScopeRelationRepository(entityOperations)
    protected val scopeTokenRepository = ScopeTokenRepository(entityOperations)
    protected val userRepository = UserRepository(entityOperations, eventPublisher = eventEmitter)
    protected val userCredentialRepository = UserCredentialRepository(entityOperations, eventPublisher = eventEmitter)
    protected val userScopeRepository = spyk(UserScopeRepository(entityOperations, eventPublisher = eventEmitter))

    protected val scopeTokenMapper = ScopeTokenMapper(
        scopeTokenRepository,
        scopeRelationRepository,
        transactionalOperator,
        eventEmitter
    )
    protected val scopeTokenStorage = ScopeTokenStorage(scopeTokenRepository, scopeTokenMapper)
    protected val scopeTokenFactory = ScopeTokenFactory(scopeTokenRepository, scopeTokenMapper, eventEmitter)

    protected val userMapper = UserMapper(userRepository, userCredentialRepository, userScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)
    protected val usersMapper = UsersMapper(userRepository, userCredentialRepository, userScopeRepository, scopeTokenStorage, transactionalOperator, eventEmitter)

    protected val userFactory = UserFactory(userRepository, userCredentialRepository, userMapper, scopeTokenStorage, transactionalOperator, eventEmitter)
    protected val userStorage = UserStorage(userRepository, userMapper, usersMapper)

    @BeforeEach
    override fun setUp() {
        super.setUp()

        blocking {
            scopeTokenFactory.upsert("user:pack")
        }
    }

    companion object {
        val helper = MongoTestHelper()

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
