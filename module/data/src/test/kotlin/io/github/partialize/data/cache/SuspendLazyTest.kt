package io.github.partialize.data.cache

import com.github.javafaker.Faker
import io.github.partialize.coroutine.test.CoroutineTestHelper
import io.github.partialize.util.username
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SuspendLazyTest : CoroutineTestHelper() {
    private val faker = Faker()

    @Test
    fun get() = blocking {
        val suspendLazy = SuspendLazy { faker.name().username(10) }
        val value = suspendLazy.get()
        assertEquals(value, suspendLazy.get())
    }

    @Test
    fun pop() = blocking {
        val suspendLazy = SuspendLazy { faker.name().username(10) }
        assertNull(suspendLazy.pop())
        val value = suspendLazy.get()
        assertEquals(value, suspendLazy.pop())
        assertNull(suspendLazy.pop())
    }

    @Test
    fun clear() = blocking {
        val suspendLazy = SuspendLazy { faker.name().username(10) }
        suspendLazy.get()
        suspendLazy.clear()
        assertNull(suspendLazy.pop())
    }
}
