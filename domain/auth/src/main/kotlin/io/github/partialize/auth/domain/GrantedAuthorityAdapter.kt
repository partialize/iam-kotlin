package io.github.partialize.auth.domain

import io.github.partialize.auth.domain.scope_token.ScopeToken
import org.springframework.security.core.GrantedAuthority

class GrantedAuthorityAdapter(
    scopeToken: ScopeToken
) : GrantedAuthority {
    private val scopeTokenId = scopeToken.id.toString()

    override fun getAuthority(): String {
        return scopeTokenId
    }
}
