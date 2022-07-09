package io.github.partialize.client.converter.r2dbc

import io.github.partialize.client.entity.ClientType
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
class ClientTypeToStringConverter : Converter<ClientType, String> {
    override fun convert(source: ClientType): String {
        return source.value
    }
}
