package io.github.partialize.auth.domain.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken

interface AuthorizeStrategy {
    suspend fun authorize(principal: Principal, scopeToken: ScopeToken, targetDomainObject: Any? = null): Boolean
}
