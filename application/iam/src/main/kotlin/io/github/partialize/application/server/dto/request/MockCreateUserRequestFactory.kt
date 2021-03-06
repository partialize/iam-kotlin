package io.github.partialize.application.server.dto.request

import com.github.javafaker.Faker
import io.github.partialize.util.resolveNotNull
import io.github.partialize.util.username
import java.util.Optional

object MockCreateUserRequestFactory {
    data class Template(
        val name: Optional<String>? = null,
        val email: Optional<String>? = null,
        val password: Optional<String>? = null,
    )

    private val faker = Faker()

    fun create(template: Template? = null): CreateUserRequest {
        return CreateUserRequest(
            name = resolveNotNull(template?.name) { faker.name().username(10) },
            email = resolveNotNull(template?.email) { faker.internet().emailAddress() },
            password = resolveNotNull(template?.password) { faker.internet().password() },
        )
    }
}
