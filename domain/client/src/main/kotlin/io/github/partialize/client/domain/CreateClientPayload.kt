package io.github.partialize.client.domain

import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.entity.ClientType
import io.github.partialize.ulid.ULID
import java.net.URL

data class CreateClientPayload(
    val name: String,
    val type: ClientType,
    val origin: URL,
    val scope: Collection<ScopeToken>? = null,
    val id: ULID? = null
)
