package io.github.partialize.auth.domain.token

import io.github.partialize.auth.entity.TokenData
import io.github.partialize.auth.repository.TokenRepository
import io.github.partialize.data.criteria.Criteria
import io.github.partialize.data.criteria.and
import io.github.partialize.data.criteria.where
import io.github.partialize.persistence.QueryStorage
import io.github.partialize.persistence.SimpleQueryStorage
import io.github.partialize.ulid.ULID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class TokenStorage(
    tokenRepository: TokenRepository,
    tokenMapper: TokenMapper
) : QueryStorage<Token, ULID> {
    private val delegator = SimpleQueryStorage(tokenRepository, { tokenMapper.map(it) })

    fun load(type: String, claims: Map<String, Any>, limit: Int? = null, offset: Long? = null, sort: Sort? = null): Flow<Token> {
        var query: Criteria = where(TokenData::type).`is`(type)
        claims.forEach { (key, value) ->
            query = query.and(where("claims.$key").`is`(value))
        }

        return load(query, limit, offset, sort)
    }

    suspend fun loadOrFail(signature: String): Token {
        return load(signature) ?: throw EmptyResultDataAccessException(1)
    }

    suspend fun load(signature: String): Token? {
        return load(where(TokenData::signature).`is`(signature))
    }

    override suspend fun load(criteria: Criteria): Token? {
        return delegator.load(criteria)?.let { if (it.isActivated()) it else null }
    }

    override fun load(criteria: Criteria?, limit: Int?, offset: Long?, sort: Sort?): Flow<Token> {
        return delegator.load(criteria, limit, offset, sort).filter { it.isActivated() }
    }

    override suspend fun count(criteria: Criteria?): Long {
        return delegator.count(criteria)
    }

    override suspend fun load(id: ULID): Token? {
        return delegator.load(id)?.let { if (it.isActivated()) it else null }
    }

    override fun load(ids: Iterable<ULID>): Flow<Token> {
        return delegator.load(ids).filter { it.isActivated() }
    }

    override suspend fun count(): Long {
        return delegator.count()
    }
}
