package io.github.partialize.ulid.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import io.github.partialize.ulid.ULID

class ULIDDeserializer : JsonDeserializer<ULID>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ULID {
        return ULID.fromString(p.valueAsString)
    }
}
