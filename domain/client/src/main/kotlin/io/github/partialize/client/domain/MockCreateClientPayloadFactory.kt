package io.github.partialize.client.domain

import com.github.javafaker.Faker
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.entity.ClientType
import io.github.partialize.util.resolve
import io.github.partialize.util.resolveNotNull
import io.github.partialize.util.url
import io.github.partialize.util.username
import java.net.URL
import java.util.Optional

object MockCreateClientPayloadFactory {
    data class Template(
        val name: Optional<String>? = null,
        val type: Optional<ClientType>? = null,
        val origin: Optional<URL>? = null,
        val scope: Optional<Collection<ScopeToken>>? = null
    )

    private val faker = Faker()

    fun create(template: Template? = null): CreateClientPayload {
        return CreateClientPayload(
            name = resolveNotNull(template?.name) { faker.name().username(10) },
            type = resolveNotNull(template?.type) { ClientType.CONFIDENTIAL },
            origin = resolveNotNull(template?.origin) { faker.internet().url(scheme = null) },
            scope = resolve(template?.scope) { null }
        )
    }
}
