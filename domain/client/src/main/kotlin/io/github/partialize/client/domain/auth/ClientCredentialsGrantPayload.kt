package io.github.partialize.client.domain.auth

import io.github.partialize.ulid.ULID

data class ClientCredentialsGrantPayload(
    val id: ULID,
    val secret: String?,
)
