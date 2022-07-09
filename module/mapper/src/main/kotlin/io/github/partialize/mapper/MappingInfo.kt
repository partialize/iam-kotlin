package io.github.partialize.mapper

import java.lang.reflect.Type

data class MappingInfo(
    val source: Type,
    val target: Type
)
