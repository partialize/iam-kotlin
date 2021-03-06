package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.ScopeTokenInfo
import io.github.partialize.application.server.dto.response.UserInfo
import io.github.partialize.auth.domain.SuspendSecurityContextHolder
import io.github.partialize.auth.domain.authorization.Authorizator
import io.github.partialize.auth.domain.scope_token.ScopeToken
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.ProjectNode
import io.github.partialize.presentation.project.Projection
import io.github.partialize.presentation.project.project
import io.github.partialize.user.domain.User
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class UserInfoMapper(
    private val mapperContext: MapperContext,
    private val authorizator: Authorizator,
) : Mapper<Projection<User>, UserInfo> {
    override val sourceType = object : TypeReference<Projection<User>>() {}
    override val targetType = object : TypeReference<UserInfo>() {}

    override suspend fun map(source: Projection<User>): UserInfo {
        val node = source.node
        val value = source.value
        val raw = value.raw()
        return UserInfo(
            id = node.project(UserInfo::id) { raw.id },
            name = node.project(UserInfo::name) { raw.name },
            email = node.project(UserInfo::email) { raw.email },
            scope = node.project(UserInfo::scope) { getScope(source.value, it) },
            createdAt = node.project(UserInfo::createdAt) { raw.createdAt },
            updatedAt = node.project(UserInfo::createdAt) { raw.updatedAt }
        )
    }

    private suspend fun getScope(value: User, node: ProjectNode): Collection<ScopeTokenInfo>? {
        return if (authorize(value)) {
            mapperContext.map(Projection(value.getScope(deep = false).toList() as Collection<ScopeToken>, node))
        } else {
            null
        }
    }

    private suspend fun authorize(source: User): Boolean {
        val principal = SuspendSecurityContextHolder.getPrincipal() ?: return false
        return authorizator.authorize(
            principal,
            listOf("users[self].scope:read", "users.scope:read"),
            listOf(source.id, null)
        )
    }
}
