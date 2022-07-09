package io.github.partialize.data.converter

import io.github.partialize.data.annotation.ConverterScope
import io.github.partialize.ulid.ULID
import org.bson.types.Binary
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
@ConverterScope(ConverterScope.Type.MONGO)
class ULIDToBinaryConverter : Converter<ULID, Binary> {
    override fun convert(source: ULID): Binary {
        return Binary(source.toBytes())
    }
}
