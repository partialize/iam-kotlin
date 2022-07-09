package io.github.partialize.user.entity

import io.github.partialize.data.ModifiableLongIDEntity
import io.github.partialize.data.annotation.Key
import io.github.partialize.ulid.ULID
import org.springframework.data.relational.core.mapping.Table

@Table("user_scopes")
data class UserScopeData(
    @Key("business_keys")
    val userId: ULID,
    @Key("business_keys")
    val scopeTokenId: ULID,
) : ModifiableLongIDEntity()
