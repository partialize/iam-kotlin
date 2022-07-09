package io.github.partialize.auth.domain.scope_token

data class CreateScopeTokenPayload(
    var name: String,
    var description: String? = null,
    var system: Boolean = true,
)
