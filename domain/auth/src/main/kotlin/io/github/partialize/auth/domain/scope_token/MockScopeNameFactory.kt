package io.github.partialize.auth.domain.scope_token

import com.github.javafaker.Faker
import io.github.partialize.util.word

object MockScopeNameFactory {
    private val faker = Faker()

    fun create(size: Int = 10, action: String = "test") = "${faker.lorem().word(size - action.length)}:$action"
}
