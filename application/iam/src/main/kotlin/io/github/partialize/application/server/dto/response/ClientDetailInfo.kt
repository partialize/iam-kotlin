package io.github.partialize.application.server.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.partialize.client.entity.ClientType
import io.github.partialize.ulid.ULID
import java.net.URL
import java.time.Instant
import java.util.Optional

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ClientDetailInfo(
    val id: Optional<ULID>?,
    val name: Optional<String>?,
    val type: Optional<ClientType>?,
    var origin: Optional<URL>?,
    val secret: Optional<String>?,
    val scope: Optional<Collection<ScopeTokenInfo>>?,
    val createdAt: Optional<Instant>?,
    val updatedAt: Optional<Instant>?,
)
