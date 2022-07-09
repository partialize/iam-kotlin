package io.github.partialize.auth.domain.authorization

import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken
import org.springframework.stereotype.Component

@Component
class AllowAllAuthorizeFilter : AuthorizeFilter {
    override fun isSubscribe(principal: Principal, scopeToken: ScopeToken): Boolean {
        return true
    }
}
