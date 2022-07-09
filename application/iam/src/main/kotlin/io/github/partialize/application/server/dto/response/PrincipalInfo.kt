package io.github.partialize.application.server.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.partialize.ulid.ULID
import java.util.Optional

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PrincipalInfo(
    val id: Optional<ULID>?,
    val type: Optional<String>?,
    val claims: Optional<Map<String, Any>>?,
    val scope: Optional<Collection<ScopeTokenInfo>>?
)
