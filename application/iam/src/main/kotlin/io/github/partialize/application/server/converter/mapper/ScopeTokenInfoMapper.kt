package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.ScopeTokenInfo
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.TypeReference
import io.github.partialize.presentation.project.Projection
import io.github.partialize.presentation.project.project
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Component

@Component
class ScopeTokenInfoMapper : Mapper<Projection<ScopeToken>, ScopeTokenInfo> {
    override val sourceType = object : TypeReference<Projection<ScopeToken>>() {}
    override val targetType = object : TypeReference<ScopeTokenInfo>() {}

    override suspend fun map(source: Projection<ScopeToken>): ScopeTokenInfo {
        return map(source, null)
    }

    suspend fun map(source: Projection<ScopeToken>, relations: Set<Pair<ScopeToken, ScopeToken>>?): ScopeTokenInfo {
        val node = source.node
        val value = source.value
        val raw = value.raw()
        return ScopeTokenInfo(
            id = node.project(ScopeTokenInfo::id) { raw.id },
            name = node.project(ScopeTokenInfo::name) { raw.name },
            description = node.project(ScopeTokenInfo::description) { raw.description },
            system = node.project(ScopeTokenInfo::system) { raw.system },
            children = node.project(ScopeTokenInfo::children) {
                if (value.isPacked()) {
                    val finalRelations = relations ?: value.relations().toSet()
                    finalRelations
                        .filter { (parent, _) -> parent.id == value.id }
                        .map { (_, child) -> map(Projection(child, it), finalRelations) }
                } else {
                    null
                }
            },
            createdAt = node.project(ScopeTokenInfo::createdAt) { raw.createdAt },
            updatedAt = node.project(ScopeTokenInfo::updatedAt) { raw.updatedAt },
        )
    }
}
