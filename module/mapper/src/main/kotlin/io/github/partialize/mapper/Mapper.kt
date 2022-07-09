package io.github.partialize.mapper

interface Mapper<SOURCE : Any, TARGET : Any> {
    val sourceType: TypeReference<SOURCE>
    val targetType: TypeReference<TARGET>

    suspend fun map(source: SOURCE): TARGET
}
