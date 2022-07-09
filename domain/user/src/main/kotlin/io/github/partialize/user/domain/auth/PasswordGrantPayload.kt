package io.github.partialize.user.domain.auth

import io.github.partialize.ulid.ULID

data class PasswordGrantPayload(
    val username: String,
    val password: String,
    val clientId: ULID?
)
