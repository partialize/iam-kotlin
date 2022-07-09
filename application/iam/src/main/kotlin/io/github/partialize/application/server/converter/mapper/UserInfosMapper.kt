package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.UserInfo
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.Projection
import io.github.partialize.user.domain.User
import org.springframework.stereotype.Component

@Component
class UserInfosMapper(
    private val mapperContext: MapperContext
) : Mapper<Projection<Collection<User>>, Collection<UserInfo>> {
    override val sourceType = object : TypeReference<Projection<Collection<User>>>() {}
    override val targetType = object : TypeReference<Collection<UserInfo>>() {}

    override suspend fun map(source: Projection<Collection<User>>): Collection<UserInfo> {
        return source.value.map { mapperContext.map(Projection(it, source.node)) }
    }
}
