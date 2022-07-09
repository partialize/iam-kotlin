package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.ClientInfo
import io.github.partialize.application.server.dto.response.ScopeTokenInfo
import io.github.partialize.auth.domain.SuspendSecurityContextHolder
import io.github.partialize.auth.domain.authorization.Authorizator
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.client.domain.Client
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.ProjectNode
import io.github.partialize.presentation.project.Projection
import io.github.partialize.presentation.project.project
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class ClientInfoMapper(
    private val mapperContext: MapperContext,
    private val authorizator: Authorizator,
) : Mapper<Projection<Client>, ClientInfo> {
    override val sourceType = object : TypeReference<Projection<Client>>() {}
    override val targetType = object : TypeReference<ClientInfo>() {}

    override suspend fun map(source: Projection<Client>): ClientInfo {
        val node = source.node
        val value = source.value
        val raw = value.raw()
        return ClientInfo(
            id = node.project(ClientInfo::id) { raw.id },
            name = node.project(ClientInfo::name) { raw.name },
            type = node.project(ClientInfo::type) { raw.type },
            origin = node.project(ClientInfo::origin) { raw.origin },
            scope = node.project(ClientInfo::scope) { getScope(value, it) },
            createdAt = node.project(ClientInfo::createdAt) { raw.createdAt },
            updatedAt = node.project(ClientInfo::updatedAt) { raw.updatedAt },
        )
    }

    private suspend fun getScope(value: Client, node: ProjectNode): Collection<ScopeTokenInfo>? {
        return if (authorize(value)) {
            mapperContext.map(Projection(value.getScope(deep = false).toList() as Collection<ScopeToken>, node))
        } else {
            null
        }
    }

    private suspend fun authorize(source: Client): Boolean {
        val principal = SuspendSecurityContextHolder.getPrincipal() ?: return false
        return authorizator.authorize(
            principal,
            listOf("clients[self].scope:read", "clients.scope:read"),
            listOf(source.id, null)
        )
    }
}
