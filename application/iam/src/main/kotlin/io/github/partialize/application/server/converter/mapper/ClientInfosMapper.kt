package io.github.partialize.application.server.converter.mapper

import io.github.partialize.application.server.dto.response.ClientInfo
import io.github.partialize.client.domain.Client
import io.github.partialize.mapper.Mapper
import io.github.partialize.mapper.MapperContext
import io.github.partialize.mapper.TypeReference
import io.github.partialize.mapper.map
import io.github.partialize.presentation.project.Projection
import org.springframework.stereotype.Component

@Component
class ClientInfosMapper(
    private val mapperContext: MapperContext
) : Mapper<Projection<Collection<Client>>, Collection<ClientInfo>> {
    override val sourceType = object : TypeReference<Projection<Collection<Client>>>() {}
    override val targetType = object : TypeReference<Collection<ClientInfo>>() {}

    override suspend fun map(source: Projection<Collection<Client>>): Collection<ClientInfo> {
        return source.value.map { mapperContext.map(Projection(it, source.node)) }
    }
}
