package io.github.partialize.client.domain

import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.client.entity.ClientData
import io.github.partialize.client.repository.ClientCredentialRepository
import io.github.partialize.client.repository.ClientRepository
import io.github.partialize.client.repository.ClientScopeRepository
import io.github.partialize.data.aggregation.FetchContextProvider
import io.github.partialize.event.EventPublisher
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.TypeReference
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class ClientsMapper(
    private val clientRepository: ClientRepository,
    private val clientCredentialRepository: ClientCredentialRepository,
    private val clientScopeRepository: ClientScopeRepository,
    private val scopeTokenStorage: ScopeTokenStorage,
    private val operator: TransactionalOperator,
    private val eventPublisher: EventPublisher
) : Mapper<Collection<ClientData>, Collection<Client>> {
    override val sourceType = object : TypeReference<Collection<ClientData>>() {}
    override val targetType = object : TypeReference<Collection<Client>>() {}

    override suspend fun map(source: Collection<ClientData>): Collection<Client> {
        val fetchContextProvider = FetchContextProvider()

        return source.map {
            Client(
                it,
                clientRepository,
                clientCredentialRepository,
                clientScopeRepository,
                scopeTokenStorage,
                fetchContextProvider,
                operator,
                eventPublisher
            )
        }
    }
}
