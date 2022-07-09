package io.github.partialize.auth.domain.token

interface ClaimEmbedFilter {
    fun isSubscribe(principal: Any): Boolean
}
