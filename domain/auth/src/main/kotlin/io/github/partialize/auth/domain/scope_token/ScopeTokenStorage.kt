package io.github.partialize.auth.domain.scope_token

import io.github.partialize.auth.entity.ScopeTokenData
import io.github.partialize.auth.repository.ScopeTokenRepository
import io.github.partialize.data.criteria.where
import io.github.partialize.persistence.QueryStorage
import io.github.partialize.persistence.SimpleQueryStorage
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.Flow
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ScopeTokenStorage(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenMapper: ScopeTokenMapper
) : QueryStorage<ScopeToken, ULID> by SimpleQueryStorage(
    scopeTokenRepository,
    { scopeTokenMapper.map(it) }
) {
    suspend fun load(name: String): ScopeToken? {
        return load(where(ScopeTokenData::name).`is`(name))
    }

    fun load(
        name: List<String>,
        limit: Int? = null,
        offset: Long? = null,
        sort: Sort? = null
    ): Flow<ScopeToken> {
        return load(
            where(ScopeTokenData::name).`in`(name),
            limit,
            offset,
            sort
        )
    }
}

suspend fun ScopeTokenStorage.loadOrFail(name: String): ScopeToken {
    return load(name) ?: throw EmptyResultDataAccessException(1)
}
