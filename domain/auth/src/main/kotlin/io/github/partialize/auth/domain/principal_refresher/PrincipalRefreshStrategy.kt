package io.github.partialize.auth.domain.principal_refresher

import io.github.partialize.auth.domain.Principal

interface PrincipalRefreshStrategy<T : Principal> {
    suspend fun refresh(principal: T): T
}
