package io.github.partialize.data.dummy

import com.github.javafaker.Faker
import io.github.partialize.data.entity.Person
import io.github.partialize.util.resolveNotNull
import io.github.partialize.util.username
import java.util.Optional
import kotlin.random.Random

object DummyPerson {
    data class PersonTemplate(
        val name: Optional<String>? = null,
        val age: Optional<Int>? = null,
    )

    private val faker = Faker()

    fun create(template: PersonTemplate? = null): Person {
        return Person(
            name = resolveNotNull(template?.name) { faker.name().username(15) },
            age = resolveNotNull(template?.age) { Random.nextInt() },
        )
    }
}
