package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.ScopeTokenInfo
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.Projection
import org.springframework.stereotype.Component

@Component
class ScopeTokenInfosMapper(
    private val mapperContext: MapperContext
) : Mapper<Projection<Collection<ScopeToken>>, Collection<ScopeTokenInfo>> {
    override val sourceType = object : TypeReference<Projection<Collection<ScopeToken>>>() {}
    override val targetType = object : TypeReference<Collection<ScopeTokenInfo>>() {}

    override suspend fun map(source: Projection<Collection<ScopeToken>>): Collection<ScopeTokenInfo> {
        return source.value.map { mapperContext.map(Projection(it, source.node)) }
    }
}
