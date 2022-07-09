package io.github.partialize.application.server.dto.request

import com.github.javafaker.Faker
import io.github.partialize.auth.domain.scope_token.MockScopeNameFactory
import io.github.partialize.util.resolve
import io.github.partialize.util.resolveNotNull
import java.util.Optional

object MockCreateScopeTokenRequestFactory {
    data class Template(
        val name: Optional<String>? = null,
        val description: Optional<String>? = null,
    )

    private val faker = Faker()

    fun create(template: Template? = null): CreateScopeTokenRequest {
        return CreateScopeTokenRequest(
            name = resolveNotNull(template?.name) { MockScopeNameFactory.create() },
            description = resolve(template?.name) { faker.lorem().sentence() },
        )
    }
}
