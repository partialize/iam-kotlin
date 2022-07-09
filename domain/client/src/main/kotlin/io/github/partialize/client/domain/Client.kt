package io.github.partialize.client.domain

import io.github.partialize.auth.domain.authorization.Authorizable
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.client.domain.auth.ClientPrincipal
import io.github.partialize.client.entity.ClientData
import io.github.partialize.client.entity.ClientEntity
import io.github.partialize.client.entity.ClientScopeData
import io.github.partialize.client.entity.ClientType
import io.github.partialize.client.repository.ClientCredentialRepository
import io.github.partialize.client.repository.ClientRepository
import io.github.partialize.client.repository.ClientScopeRepository
import io.github.partialize.data.aggregation.FetchContextProvider
import io.github.partialize.data.aggregation.get
import io.github.partialize.data.cache.SuspendLazy
import io.github.partialize.data.criteria.and
import io.github.partialize.data.criteria.where
import io.github.partialize.data.repository.findOneOrFail
import io.github.partialize.event.EventPublisher
import io.github.partialize.persistence.Persistence
import io.github.partialize.persistence.PersistencePropagateSynchronization
import io.github.partialize.persistence.PersistenceSynchronization
import io.github.partialize.persistence.proxy
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.transaction.reactive.TransactionalOperator

class Client(
    value: ClientData,
    clientRepository: ClientRepository,
    private val clientCredentialRepository: ClientCredentialRepository,
    private val clientScopeRepository: ClientScopeRepository,
    private val scopeTokenStorage: ScopeTokenStorage,
    fetchContextProvider: FetchContextProvider,
    operator: TransactionalOperator,
    private val eventPublisher: EventPublisher
) : Persistence<ClientData, ULID>(
    value,
    clientRepository,
    operator,
    eventPublisher
),
    ClientEntity,
    Authorizable {
    val id by proxy(root, ClientData::id)
    var name by proxy(root, ClientData::name)
    val type by proxy(root, ClientData::type)
    var origin by proxy(root, ClientData::origin)

    override val clientId by proxy(root, ClientData::id)

    private val scopeContext = fetchContextProvider.get(clientScopeRepository)
    private val scopeFetcher = scopeContext.join(
        where(ClientScopeData::clientId).`is`(clientId)
    )

    private val credential = SuspendLazy {
        ClientCredential(
            clientCredentialRepository.findByClientIdOrFail(clientId),
            clientCredentialRepository,
            eventPublisher
        ).also {
            synchronize(PersistencePropagateSynchronization(it))
        }
    }

    init {
        synchronize(
            object : PersistenceSynchronization {
                override suspend fun beforeClear() {
                    scopeFetcher.clear()

                    clientScopeRepository.deleteAllByClientId(id)
                    if (isConfidential()) {
                        credential.get().clear()
                    }
                }

                override suspend fun afterClear() {
                    credential.clear()
                }
            }
        )
    }

    fun isConfidential(): Boolean {
        return type == ClientType.CONFIDENTIAL
    }

    fun isPublic(): Boolean {
        return type == ClientType.PUBLIC
    }

    override suspend fun has(scopeToken: ScopeToken): Boolean {
        return clientScopeRepository.exists(
            where(ClientScopeData::clientId).`is`(id)
                .and(where(ClientScopeData::scopeTokenId).`is`(scopeToken.id))
        )
    }

    override suspend fun grant(scopeToken: ScopeToken) {
        clientScopeRepository.create(
            ClientScopeData(
                clientId = id,
                scopeTokenId = scopeToken.id
            )
        ).also { scopeContext.clear(it) }
    }

    override suspend fun revoke(scopeToken: ScopeToken) {
        val clientScope = clientScopeRepository.findOneOrFail(
            where(ClientScopeData::clientId).`is`(id)
                .and(where(ClientScopeData::scopeTokenId).`is`(scopeToken.id))
        )
        scopeContext.clear(clientScope)
        clientScopeRepository.delete(clientScope)
    }

    suspend fun getCredential(): ClientCredential {
        return credential.get()
            .also { it.link() }
    }

    fun getScope(deep: Boolean = true): Flow<ScopeToken> {
        return flow {
            val scopeTokenIds = scopeFetcher.fetch()
                .map { it.scopeTokenId }
                .toList()

            scopeTokenStorage.load(scopeTokenIds)
                .collect {
                    if (deep) {
                        emitAll(it.resolve())
                    } else {
                        emit(it)
                    }
                }
        }
    }

    suspend fun toPrincipal(
        push: List<ScopeToken> = emptyList(),
        pop: List<ScopeToken> = emptyList()
    ): ClientPrincipal {
        val myScope = getScope()
        val scope = mutableSetOf<ScopeToken>()

        scope.addAll(
            myScope
                .filter { token -> pop.firstOrNull { it.id == token.id } == null }
                .toList()
        )
        scope.addAll(push.toList())

        return ClientPrincipal(
            id = id,
            clientId = clientId,
            scope = scope.toSet()
        )
    }
}
