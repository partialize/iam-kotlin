package io.github.partialize.auth.domain.authentication

import com.github.javafaker.Faker
import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.auth.exception.AuthorizeException
import io.github.partialize.auth.exception.UnauthorizatedException
import io.github.partialize.coroutine.test.CoroutineTestHelper
import io.github.partialize.ulid.ULID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticatorTest : CoroutineTestHelper() {
    internal class TestAuthorizationStrategy(
        type: String,
        private val credentials: String
    ) : AuthorizationStrategy<Principal>(type) {
        override suspend fun authenticate(credentials: String): Principal {
            if (this.credentials != credentials) {
                throw AuthorizeException()
            }

            return object : Principal {
                override val id = ULID.randomULID()
                override var scope = emptySet<ScopeToken>()
            }
        }
    }

    internal class TestAuthenticatePipeline : AuthenticatePipeline<Principal> {
        override val clazz = Principal::class

        override suspend fun pipe(principal: Principal): Principal {
            return principal
        }
    }

    private val faker = Faker()
    private val filter = AllowAllAuthenticateFilter()

    @Test
    fun registerStrategy() {
        val authenticator = Authenticator()
        authenticator.register(filter, TestAuthorizationStrategy("", ""))
    }

    @Test
    fun registerPipeline() {
        val authenticator = Authenticator()
        authenticator.register(filter, TestAuthenticatePipeline())
    }

    @Test
    fun authenticate() = blocking {
        val authenticator = Authenticator()

        val type = faker.random().hex()
        val credentials = faker.random().hex()

        assertThrows<UnauthorizatedException> { authenticator.authenticate(AuthorizationPayload(type, credentials)) }

        authenticator.register(filter, TestAuthorizationStrategy(type, credentials))
        authenticator.authenticate(AuthorizationPayload(type, credentials))
    }
}
