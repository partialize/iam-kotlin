package io.github.partialize.user.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class UserData(
    @Key
    var name: String,
    @Key
    var email: String,
) : ModifiableULIDEntity()
