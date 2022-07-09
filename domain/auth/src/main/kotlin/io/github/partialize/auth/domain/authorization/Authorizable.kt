package io.github.partialize.auth.domain.authorization

import io.github.partialize.auth.domain.scope_token.ScopeToken

interface Authorizable {
    suspend fun has(scopeToken: ScopeToken): Boolean
    suspend fun grant(scopeToken: ScopeToken)
    suspend fun revoke(scopeToken: ScopeToken)
}
