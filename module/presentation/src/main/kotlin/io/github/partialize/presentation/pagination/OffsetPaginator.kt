package io.github.partialize.presentation.pagination

import io.github.partialize.data.criteria.Criteria
import io.github.partialize.persistence.QueryStorage
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Sort

class OffsetPaginator<T : Any, ID : Any>(
    private val storage: QueryStorage<T, ID>,
) {
    suspend fun paginate(
        criteria: Criteria? = null,
        sort: Sort? = null,
        page: Int?,
        perPage: Int?
    ): OffsetPage<T> {
        val finalPage = page ?: 0
        val finalPerPage = (perPage ?: 15).coerceAtMost(150)

        val data = storage.load(
            criteria,
            limit = finalPerPage,
            offset = (finalPage * finalPerPage).toLong(),
            sort = sort
        ).toList()

        val total = if (page == null) {
            null
        } else {
            storage.count(criteria)
        }

        return OffsetPage(
            data = data,
            total = total,
            perPage = finalPerPage,
            page = page
        )
    }
}
