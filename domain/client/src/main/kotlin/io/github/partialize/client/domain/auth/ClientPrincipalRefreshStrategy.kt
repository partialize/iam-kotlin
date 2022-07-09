package io.github.partialize.client.domain.auth

import io.github.partialize.auth.domain.PrincipalMapping
import io.github.partialize.auth.domain.principal_refresher.PrincipalRefreshStrategy
import io.github.partialize.client.domain.ClientStorage
import io.github.partialize.persistence.loadOrFail
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Component

@Component
@PrincipalMapping(ClientPrincipal::class)
class ClientPrincipalRefreshStrategy(
    private val clientStorage: ClientStorage,
) : PrincipalRefreshStrategy<ClientPrincipal> {

    override suspend fun refresh(principal: ClientPrincipal): ClientPrincipal {
        val client = clientStorage.loadOrFail(principal.clientId)
        val clientScope = client.getScope().toSet()

        return ClientPrincipal(
            id = principal.id,
            clientId = principal.clientId,
            scope = clientScope
        )
    }
}
