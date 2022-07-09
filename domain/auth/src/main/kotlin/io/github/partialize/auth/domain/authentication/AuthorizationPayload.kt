package io.github.partialize.auth.domain.authentication

data class AuthorizationPayload(
    val type: String,
    val credentials: String,
)
