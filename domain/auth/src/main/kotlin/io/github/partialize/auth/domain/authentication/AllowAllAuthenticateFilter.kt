package io.github.partialize.auth.domain.authentication

import org.springframework.stereotype.Component

@Component
class AllowAllAuthenticateFilter : AuthenticateFilter {
    override fun isSubscribe(payload: Any): Boolean {
        return true
    }
}
