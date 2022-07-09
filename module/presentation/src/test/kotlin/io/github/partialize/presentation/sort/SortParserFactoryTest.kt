package io.github.partialize.presentation.sort

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.partialize.presentation.entity.Person
import org.junit.jupiter.api.Test

class SortParserFactoryTest {
    private val objectMapper = ObjectMapper()
    private val sortParserFactory = SortParserFactory(objectMapper)

    @Test
    fun create() {
        sortParserFactory.create(Person::class)
    }
}
