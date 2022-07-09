package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authentication.AuthenticateMapping
import io.github.partialize.auth.domain.authentication.AuthenticateStrategy
import io.github.partialize.client.domain.ClientStorage
import io.github.partialize.persistence.loadOrFail
import io.github.partialize.user.domain.UserStorage
import io.github.partialize.user.domain.loadOrFail
import io.github.partialize.user.exception.IncorrectPasswordException
import org.springframework.stereotype.Component

@Component
@AuthenticateMapping
class PasswordGrantAuthenticateStrategy(
    private val userStorage: UserStorage,
    private val clientStorage: ClientStorage
) : AuthenticateStrategy<PasswordGrantPayload, UserPrincipal> {
    override val clazz = PasswordGrantPayload::class

    override suspend fun authenticate(payload: PasswordGrantPayload): UserPrincipal? {
        val user = userStorage.loadOrFail(payload.username)
        val credential = user.getCredential()
        val client = payload.clientId?.let { clientStorage.loadOrFail(it) }

        if (!credential.isPassword(payload.password)) {
            throw IncorrectPasswordException()
        }

        return user.toPrincipal(clientEntity = client)
    }
}
