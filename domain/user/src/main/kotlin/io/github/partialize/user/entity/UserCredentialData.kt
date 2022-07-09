package io.github.partialize.user.entity

import io.github.partialize.data.ModifiableLongIDEntity
import io.github.partialize.data.annotation.Key
import io.github.partialize.ulid.ULID
import org.springframework.data.relational.core.mapping.Table

@Table("user_credentials")
data class UserCredentialData(
    @Key
    val userId: ULID,
    var password: String,
    var hashAlgorithm: String,
) : ModifiableLongIDEntity()
