package io.github.partialize.application.server.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.partialize.ulid.ULID
import java.net.URL
import java.util.Optional
import javax.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateClientRequest(
    val name: Optional<@Size(min = 3, max = 20) String>? = null,
    val origin: Optional<@Size(max = 2048) URL>? = null,
    val scope: Optional<Collection<ULID>>? = null
)
