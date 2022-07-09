package io.github.partialize.application.server.auth.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.authorization.AuthorizeMapping
import io.github.partialize.auth.domain.authorization.AuthorizeStrategy
import io.github.partialize.auth.domain.authorization.ScopeMapping
import io.github.partialize.auth.domain.authorization.ScopeMatchAuthorizeFilter
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.entity.ClientEntity
import io.github.partialize.ulid.ULID
import org.springframework.stereotype.Component

@Component
@AuthorizeMapping(ScopeMatchAuthorizeFilter::class)
@ScopeMapping(
    [
        "clients[self]:read",
        "clients[self]:update",
        "clients[self]:delete",
        "clients[self].scope:read"
    ]
)
class ClientIdMatchAuthorizeStrategy : AuthorizeStrategy {
    override suspend fun authorize(
        principal: Principal,
        scopeToken: ScopeToken,
        targetDomainObject: Any?
    ): Boolean {
        val clientEntity = principal as? ClientEntity ?: return false
        val id = targetDomainObject as? ULID ?: return true

        return clientEntity.clientId == id
    }
}
