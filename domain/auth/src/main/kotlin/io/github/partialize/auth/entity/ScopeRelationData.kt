package io.github.partialize.auth.entity

import io.github.partialize.data.ModifiableLongIDEntity
import io.github.partialize.data.annotation.Key
import io.github.partialize.ulid.ULID
import org.springframework.data.relational.core.mapping.Table

@Table("scope_relations")
data class ScopeRelationData(
    @Key("business_keys")
    val parentId: ULID,
    @Key("business_keys")
    val childId: ULID,
) : ModifiableLongIDEntity()
