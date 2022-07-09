package io.github.partialize.client.domain.auth

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.entity.ClientEntity
import io.github.partialize.ulid.ULID

data class ClientPrincipal(
    override val id: ULID = ULID.randomULID(),
    override val clientId: ULID,
    override var scope: Set<ScopeToken>,
) : Principal, ClientEntity
