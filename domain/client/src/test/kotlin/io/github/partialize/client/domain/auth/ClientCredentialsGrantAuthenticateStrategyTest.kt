package io.github.partialize.client.domain.auth

import io.github.partialize.client.domain.ClientTestHelper
import io.github.partialize.client.domain.MockCreateClientPayloadFactory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class ClientCredentialsGrantAuthenticateStrategyTest : ClientTestHelper() {
    private val authorizationStrategy = ClientCredentialsGrantAuthenticateStrategy(clientStorage)

    @Test
    fun authenticate() = blocking {
        val client = MockCreateClientPayloadFactory.create()
            .let { clientFactory.create(it) }

        val principal = authorizationStrategy.authenticate(
            ClientCredentialsGrantPayload(
                client.id, client.getCredential().raw().secret
            )
        )
        assertNotNull(principal)
    }
}
