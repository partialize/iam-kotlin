package io.github.partialize.user.domain

import io.github.partialize.auth.domain.hash
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.domain.scope_token.loadOrFail
import io.github.partialize.data.cache.SuspendLazy
import io.github.partialize.data.event.AfterCreateEvent
import io.github.partialize.event.EventPublisher
import io.github.partialize.user.entity.UserCredentialData
import io.github.partialize.user.entity.UserData
import io.github.partialize.user.repository.UserCredentialRepository
import io.github.partialize.user.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.security.MessageDigest

@Component
class UserFactory(
    private val userRepository: UserRepository,
    private val userCredentialRepository: UserCredentialRepository,
    private val userMapper: UserMapper,
    private val scopeTokenStorage: ScopeTokenStorage,
    private val operator: TransactionalOperator,
    private val eventPublisher: EventPublisher,
    private val hashAlgorithm: String = "SHA-256"
) {
    private val defaultScope = SuspendLazy {
        scopeTokenStorage.loadOrFail("user:pack")
    }

    suspend fun create(payload: CreateUserPayload): User =
        operator.executeAndAwait {
            val user = createUser(payload)

            user.link()
            createCredential(user, payload)

            if (payload.scope == null) {
                val scope = defaultScope.get()
                user.grant(scope)
            } else {
                payload.scope.forEach {
                    user.grant(it)
                }
            }

            eventPublisher.publish(AfterCreateEvent(user))

            user
        }!!

    private suspend fun createUser(payload: CreateUserPayload): User {
        return UserData(payload.name, payload.email)
            .let { userRepository.create(it) }
            .let { userMapper.map(it) }
    }

    private suspend fun createCredential(user: User, payload: CreateUserPayload): UserCredentialData {
        val messageDigest = MessageDigest.getInstance(hashAlgorithm)
        val password = messageDigest.hash(payload.password)

        return userCredentialRepository.create(
            UserCredentialData(
                userId = user.id,
                password = password,
                hashAlgorithm = hashAlgorithm
            )
        )
    }
}
