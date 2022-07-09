package io.github.partialize.client.entity

import io.github.partialize.data.ModifiableLongIDEntity
import io.github.partialize.data.annotation.Key
import io.github.partialize.ulid.ULID
import org.springframework.data.relational.core.mapping.Table

@Table("client_credentials")
data class ClientCredentialData(
    @Key
    val clientId: ULID,
    var secret: String,
) : ModifiableLongIDEntity()
