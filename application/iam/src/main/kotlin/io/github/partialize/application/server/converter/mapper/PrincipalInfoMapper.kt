package io.github.partialize.application.server.converter.mapper

import com.google.common.base.CaseFormat
import com.google.common.collect.Maps
import io.github.partialize.application.server.dto.response.PrincipalInfo
import io.github.partialize.auth.domain.Principal
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.auth.domain.token.ClaimEmbedder
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.Projection
import io.github.partialize.presentation.project.project
import org.springframework.stereotype.Component

@Component
class PrincipalInfoMapper(
    private val mapperContext: MapperContext,
    private val claimEmbedder: ClaimEmbedder
) : Mapper<Projection<Principal>, PrincipalInfo> {
    override val sourceType = object : TypeReference<Projection<Principal>>() {}
    override val targetType = object : TypeReference<PrincipalInfo>() {}

    private val typeMapping = Maps.newConcurrentMap<Class<*>, String>()

    override suspend fun map(source: Projection<Principal>): PrincipalInfo {
        val node = source.node
        val value = source.value

        return PrincipalInfo(
            id = node.project(PrincipalInfo::id) { value.id },
            type = node.project(PrincipalInfo::type) {
                typeMapping.getOrPut(value.javaClass) {
                    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value.javaClass.simpleName)
                }
            },
            claims = node.project(PrincipalInfo::claims) {
                claimEmbedder.embedding(value)
            },
            scope = node.project(PrincipalInfo::scope) {
                mapperContext.map(Projection(value.scope as Collection<ScopeToken>, it))
            },
        )
    }
}
