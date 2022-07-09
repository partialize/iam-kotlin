package io.github.partialize.data.criteria

interface CriteriaParser<Out : Any?> {
    fun parse(criteria: Criteria): Out
}
