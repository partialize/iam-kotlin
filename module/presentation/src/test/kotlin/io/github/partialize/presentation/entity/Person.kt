package io.github.partialize.presentation.entity

import io.github.partialize.data.ModifiableULIDEntity
import io.github.partialize.data.annotation.Key
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.relational.core.mapping.Table

@Table("persons")
@Document("persons")
data class Person(
    @Key
    var name: String,
    var age: Int
) : ModifiableULIDEntity()
