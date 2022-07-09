package io.github.partialize.user.domain

import io.github.partialize.auth.domain.scope_token.ScopeToken

data class CreateUserPayload(
    val name: String,
    val email: String,
    val password: String,
    val scope: Collection<ScopeToken>? = null
)
