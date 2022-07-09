package io.github.partialize.client.domain

import io.github.partialize.client.entity.ClientCredentialData
import io.github.partialize.client.repository.ClientCredentialRepository
import io.github.partialize.event.EventPublisher
import io.github.partialize.persistence.Persistence
import io.github.partialize.persistence.proxy
import io.github.partialize.persistence.proxyNotNull

class ClientCredential(
    value: ClientCredentialData,
    clientCredentialRepository: ClientCredentialRepository,
    eventPublisher: EventPublisher
) : Persistence<ClientCredentialData, Long>(
    value,
    clientCredentialRepository,
    eventPublisher = eventPublisher
) {
    val id by proxyNotNull(root, ClientCredentialData::id)
    val clientId by proxy(root, ClientCredentialData::clientId)

    fun checkSecret(secret: String): Boolean {
        return root[ClientCredentialData::secret] == secret
    }

    fun setSecret(secret: String) {
        root[ClientCredentialData::secret] = secret
    }
}
