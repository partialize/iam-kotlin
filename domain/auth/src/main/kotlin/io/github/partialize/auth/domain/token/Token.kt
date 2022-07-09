package io.github.partialize.auth.domain.token

import io.github.partialize.auth.domain.authorization.Authorizable
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.auth.domain.scope_token.ScopeTokenStorage
import io.github.partialize.auth.entity.TokenData
import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.data.repository.updateById
import io.github.partialize.event.EventPublisher
import io.github.partialize.persistence.Persistence
import io.github.partialize.persistence.proxy
import io.github.partialize.persistence.proxyNotNull
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toSet
import java.time.Instant

@Suppress("UNCHECKED_CAST")
class Token(
    value: TokenData,
    private val tokenRepository: TokenRepository,
    private val scopeTokenStorage: ScopeTokenStorage,
    eventPublisher: EventPublisher
) : Persistence<TokenData, ULID>(
    value,
    tokenRepository,
    eventPublisher = eventPublisher
),
    Authorizable {
    val id by proxyNotNull(root, TokenData::id)
    val type by proxy(root, TokenData::type)
    val signature by proxy(root, TokenData::signature)
    val claims by proxy(root, TokenData::claims)
    var expiredAt by proxy(root, TokenData::expiredAt)

    fun isActivated(): Boolean {
        val expiredAt = expiredAt ?: return true
        val now = Instant.now()
        return expiredAt.isAfter(now)
    }

    operator fun get(key: String): Any? {
        return claims[key]
    }

    operator fun set(key: String, value: Any?) {
        val claims = root[TokenData::claims].toMutableMap()
        if (value == null) {
            claims.remove(key)
        } else {
            claims[key] = value
        }
        root[TokenData::claims] = claims
    }

    override suspend fun has(scopeToken: ScopeToken): Boolean {
        val scope = getScope().toSet()
        return scope.contains(scopeToken)
    }

    override suspend fun grant(scopeToken: ScopeToken) {
        tokenRepository.updateById(id) {
            val claims = it.claims.toMutableMap()
            val scopeTokenIds = getScopeTokenIds(claims).toMutableList()
            scopeTokenIds.add(scopeToken.id)
            claims["scope"] = scopeTokenIds.map { it.toString() }
            it.claims = claims
        }?.let { root.raw(it) }
    }

    override suspend fun revoke(scopeToken: ScopeToken) {
        tokenRepository.updateById(id) {
            val claims = it.claims.toMutableMap()
            val scopeTokenIds = getScopeTokenIds(claims).toMutableSet()
            scopeTokenIds.remove(scopeToken.id)
            claims["scope"] = scopeTokenIds.map { it.toString() }
            it.claims = claims
        }?.let { root.raw(it) }
    }

    fun getScope(deep: Boolean = true): Flow<ScopeToken> {
        return flow {
            val claims = root[TokenData::claims]
            val scopeTokenIds = getScopeTokenIds(claims)
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

    private fun getScopeTokenIds(claims: Map<String, Any>): List<ULID> {
        val scope = (claims["scope"] as? Collection<*>) ?: emptyList<Any>()
        return scope
            .filterIsInstance<String>()
            .map { ULID.fromString(it) }
    }

    suspend fun reload() {
        tokenRepository.findById(id)?.let { root.raw(it) }
    }
}
