package io.github.partialize.persistence.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.relational.core.mapping.Table

@Table("persons")
data class PersonData(
    @Key
    var name: String,
    var age: Int
) : ModifiableULIDEntity()
