package io.github.partialize.data.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.partialize.data.annotation.ConverterScope
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
@ConverterScope(ConverterScope.Type.R2DBC)
class StringToMapConverter(
    private val objectMapper: ObjectMapper
) : Converter<String, Map<String, Any>> {
    override fun convert(source: String): Map<String, Any> {
        return objectMapper.readValue(source, object : TypeReference<Map<String, Any>>() {})
    }
}
