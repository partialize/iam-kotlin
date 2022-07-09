package io.github.partialize.client.converter.jaskcon

import com.fasterxml.jackson.databind.module.SimpleModule
import io.github.partialize.client.entity.ClientType
import org.springframework.stereotype.Component

@Component
class ClientTypeModule : SimpleModule() {
    init {
        addSerializer(ClientType::class.java, ClientTypeSerializer())
        addDeserializer(ClientType::class.java, ClientTypeDeserializer())
    }
}
