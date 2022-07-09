package io.github.partialize.auth.domain.token

import io.github.partialize.auth.domain.scope_token.ScopeToken

data class TokenTemplate(
    val type: String,
    val limit: List<Pair<String, Int>>? = null,
    val pop: Set<ScopeToken>? = null,
    val push: Set<ScopeToken>? = null,
    val filter: Set<ScopeToken>? = null,
)
