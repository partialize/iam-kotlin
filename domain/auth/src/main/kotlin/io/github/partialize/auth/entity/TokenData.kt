package io.github.partialize.auth.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("tokens")
data class TokenData(
    var type: String,
    @Key("signature")
    var signature: String,
    var claims: Map<String, Any>,
    var expiredAt: Instant? = null
) : ModifiableULIDEntity()
