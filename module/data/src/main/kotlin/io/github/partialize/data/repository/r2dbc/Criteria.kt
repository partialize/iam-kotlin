package io.github.partialize.data.repository.r2dbc

import io.github.partialize.data.expansion.columnName
import org.springframework.data.relational.core.query.Criteria
import kotlin.reflect.KProperty

fun <T> where(property: KProperty<T>): Criteria.CriteriaStep {
    return Criteria.where(columnName(property))
}
