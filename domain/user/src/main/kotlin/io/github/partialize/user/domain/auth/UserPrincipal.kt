package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.entity.ClientEntity
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserEntity

data class UserPrincipal(
    override val id: ULID = ULID.randomULID(),
    override val userId: ULID,
    override val clientId: ULID? = null,
    override var scope: Set<ScopeToken>,
) : Principal, UserEntity, ClientEntity
