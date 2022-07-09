package io.github.partialize.client.converter.jaskcon

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.partialize.client.entity.ClientType

class ClientTypeSerializer : JsonSerializer<ClientType>() {
    override fun serialize(value: ClientType, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.value)
    }
}
