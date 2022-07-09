package io.github.partialize.auth.domain.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.hasScope
import io.github.partialize.auth.domain.scope_token.ScopeToken
import org.springframework.stereotype.Component

@Component
@AuthorizeMapping(AllowAllAuthorizeFilter::class)
class PrincipalHasScopeAuthorizeStrategy : AuthorizeStrategy {
    override suspend fun authorize(principal: Principal, scopeToken: ScopeToken, targetDomainObject: Any?): Boolean {
        return principal.hasScope(scopeToken)
    }
}
