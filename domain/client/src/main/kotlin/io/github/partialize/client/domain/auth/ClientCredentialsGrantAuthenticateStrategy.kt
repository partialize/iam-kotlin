package io.github.partialize.client.domain.auth

import io.github.partialize.auth.domain.authentication.AuthenticateMapping
import io.github.partialize.auth.domain.authentication.AuthenticateStrategy
import io.github.partialize.client.domain.ClientStorage
import io.github.partialize.client.exception.SecretIncorrectException
import io.github.partialize.persistence.loadOrFail
import org.springframework.stereotype.Component

@Component
@AuthenticateMapping
class ClientCredentialsGrantAuthenticateStrategy(
    private val clientStorage: ClientStorage,
) : AuthenticateStrategy<ClientCredentialsGrantPayload, ClientPrincipal> {
    override val clazz = ClientCredentialsGrantPayload::class

    override suspend fun authenticate(payload: ClientCredentialsGrantPayload): ClientPrincipal? {
        val client = clientStorage.loadOrFail(payload.id)
        if (client.isConfidential()) {
            val credential = client.getCredential()
            if (payload.secret == null || !credential.checkSecret(payload.secret)) {
                throw SecretIncorrectException()
            }
        }

        return client.toPrincipal()
    }
}
