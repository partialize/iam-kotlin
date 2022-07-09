package io.github.partialize.auth.domain

import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.ulid.ULID

interface Principal {
    val id: ULID
    var scope: Set<ScopeToken>
}

fun Principal.hasScope(scope: Collection<ScopeToken>): Boolean {
    return this.scope.containsAll(scope)
}
fun Principal.hasScope(scopeToken: ScopeToken): Boolean {
    return this.scope.contains(scopeToken)
}
