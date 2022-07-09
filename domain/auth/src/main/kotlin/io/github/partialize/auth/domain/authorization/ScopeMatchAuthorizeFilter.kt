package io.github.partialize.auth.domain.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken

class ScopeMatchAuthorizeFilter(
    private val scopeTokens: Set<String>
) : AuthorizeFilter {
    override fun isSubscribe(principal: Principal, scopeToken: ScopeToken): Boolean {
        return scopeTokens.contains(scopeToken.name)
    }
}
