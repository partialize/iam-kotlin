package io.github.partialize.data.converter

import io.github.partialize.data.annotation.ConverterScope
import io.github.partialize.ulid.ULID
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
@ConverterScope(ConverterScope.Type.R2DBC)
class ULIDToBytesConverter : Converter<ULID, ByteArray> {
    override fun convert(source: ULID): ByteArray {
        return source.toBytes()
    }
}
