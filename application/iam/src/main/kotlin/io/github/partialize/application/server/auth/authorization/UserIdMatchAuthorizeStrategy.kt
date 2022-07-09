package io.github.partialize.application.server.auth.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.authorization.AuthorizeMapping
import io.github.partialize.auth.domain.authorization.AuthorizeStrategy
import io.github.partialize.auth.domain.authorization.ScopeMapping
import io.github.partialize.auth.domain.authorization.ScopeMatchAuthorizeFilter
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
@AuthorizeMapping(ScopeMatchAuthorizeFilter::class)
@ScopeMapping(
    [
        "users[self]:read",
        "users[self]:update",
        "users[self]:delete",
        "users[self].scope:read",
        "users[self].credential:update",
        "users[self].contact:read",
        "users[self].contact:update"
    ]
)
class UserIdMatchAuthorizeStrategy : AuthorizeStrategy {
    override suspend fun authorize(
        principal: Principal,
        scopeToken: ScopeToken,
        targetDomainObject: Any?
    ): Boolean {
        val userEntity = principal as? UserEntity ?: return false
        val id = targetDomainObject as? ULID ?: return true

        return userEntity.userId == id
    }
}
