package io.github.partialize.client.entity

import io.github.partialize.data.ModifiableLongIDEntity
import io.github.partialize.data.annotation.Key
import io.github.partialize.ulid.ULID
import org.springframework.data.relational.core.mapping.Table

@Table("client_scopes")
data class ClientScopeData(
    @Key("business_keys")
    val clientId: ULID,
    @Key("business_keys")
    val scopeTokenId: ULID,
) : ModifiableLongIDEntity()
