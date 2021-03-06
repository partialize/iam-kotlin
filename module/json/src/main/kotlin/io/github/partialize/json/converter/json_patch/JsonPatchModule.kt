package io.github.partialize.json.converter.json_patch

import com.fasterxml.jackson.databind.module.SimpleModule
import io.github.partialize.json.patch.JsonPatch
import org.springframework.stereotype.Component

@Component
class JsonPatchModule(
    jsonPatchDeserializer: JsonPatchDeserializer
) : SimpleModule() {
    init {
        addDeserializer(JsonPatch::class.java, jsonPatchDeserializer)
    }
}
