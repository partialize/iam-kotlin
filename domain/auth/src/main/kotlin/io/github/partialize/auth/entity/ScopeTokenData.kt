package io.github.partialize.auth.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.relational.core.mapping.Table

@Table("scope_tokens")
data class ScopeTokenData(
    @Key
    var name: String,
    var description: String? = null,
    val system: Boolean = true
) : ModifiableULIDEntity()
