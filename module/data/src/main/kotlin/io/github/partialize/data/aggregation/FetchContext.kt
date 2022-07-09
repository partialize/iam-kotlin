package io.github.partialize.data.aggregation

import io.github.partialize.data.cache.SelectQuery
import io.github.partialize.data.criteria.Criteria
import io.github.partialize.data.repository.QueryRepository
import kotlin.reflect.KClass

class FetchContext<T : Any>(
    repository: QueryRepository<T, *>,
    clazz: KClass<T>,
) {
    private val queryAggregator = QueryAggregator(repository, clazz)

    suspend fun clear() {
        queryAggregator.clear()
    }

    suspend fun clear(entity: T) {
        queryAggregator.clear(entity)
    }

    fun join(criteria: Criteria?, limit: Int? = null): QueryFetcher<T> {
        val query = SelectQuery(criteria, limit)
        queryAggregator.link(query)
        return QueryFetcher(query, queryAggregator)
    }
}
