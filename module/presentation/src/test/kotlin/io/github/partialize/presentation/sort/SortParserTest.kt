package io.github.partialize.presentation.sort

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.partialize.presentation.entity.Person
import io.github.partialize.presentation.exception.SortInvalidException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort.Order

class SortParserTest {
    private val objectMapper = ObjectMapper()
    private val sortParser = SortParser(Person::class, objectMapper)

    @Test
    fun parse() {
        var sort = sortParser.parse(listOf("desc:name", "asc:id"))
        assertEquals(Order.desc("name"), sort.getOrderFor("name"))
        assertEquals(Order.asc("id"), sort.getOrderFor("id"))

        sort = sortParser.parse("desc:name")
        assertEquals(Order.desc("name"), sort.getOrderFor("name"))

        assertThrows(SortInvalidException::class.java) { sortParser.parse("desc:not_found") }
        assertThrows(SortInvalidException::class.java) { sortParser.parse("not_order:name") }
    }
}
