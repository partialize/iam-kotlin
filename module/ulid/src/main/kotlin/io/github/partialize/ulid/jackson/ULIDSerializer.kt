package io.github.partialize.ulid.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.partialize.ulid.ULID

class ULIDSerializer : JsonSerializer<ULID>() {
    override fun serialize(value: ULID, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.toString())
    }
}
