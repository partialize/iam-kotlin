package io.github.partialize.client.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.relational.core.mapping.Table
import java.net.URL

@Table("clients")
data class ClientData(
    @Key
    var name: String,
    val type: ClientType,
    var origin: URL,
) : ModifiableULIDEntity()
