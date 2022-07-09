package io.github.partialize.user.domain

import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.data.aggregation.FetchContextProvider
import io.github.partialize.event.EventPublisher
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.TypeReference
import io.github.partialize.user.entity.UserData
import io.github.partialize.user.repository.UserCredentialRepository
import io.github.partialize.user.repository.UserRepository
import io.github.partialize.user.repository.UserScopeRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class UsersMapper(
    private val userRepository: UserRepository,
    private val userCredentialRepository: UserCredentialRepository,
    private val userScopeRepository: UserScopeRepository,
    private val scopeTokenStorage: ScopeTokenStorage,
    private val operator: TransactionalOperator,
    private val eventPublisher: EventPublisher
) : Mapper<Collection<UserData>, Collection<User>> {
    override val sourceType = object : TypeReference<Collection<UserData>>() {}
    override val targetType = object : TypeReference<Collection<User>>() {}

    override suspend fun map(source: Collection<UserData>): Collection<User> {
        val fetchContextProvider = FetchContextProvider()

        return source.map {
            User(
                it,
                userRepository,
                userCredentialRepository,
                userScopeRepository,
                scopeTokenStorage,
                fetchContextProvider,
                operator,
                eventPublisher
            )
        }
    }
}
