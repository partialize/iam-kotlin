package io.github.partialize.application.server.dto.request

import com.github.javafaker.Faker
import io.github.partialize.client.entity.ClientType
import io.github.partialize.util.resolveNotNull
import io.github.partialize.util.url
import io.github.partialize.util.username
import java.net.URL
import java.util.Optional

object MockCreateClientRequestFactory {
    data class Template(
        val name: Optional<String>? = null,
        val type: Optional<ClientType>? = null,
        val origin: Optional<URL>? = null
    )

    private val faker = Faker()

    fun create(template: Template? = null): CreateClientRequest {
        return CreateClientRequest(
            name = resolveNotNull(template?.name) { faker.name().username(10) },
            type = resolveNotNull(template?.type) { ClientType.PUBLIC },
            origin = resolveNotNull(template?.origin) { faker.internet().url(scheme = null) },
        )
    }
}
