package io.github.partialize.auth.domain.authentication

interface AuthenticateFilter {
    fun isSubscribe(payload: Any): Boolean
}
