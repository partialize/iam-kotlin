package io.github.partialize.user.domain

import io.github.partialize.data.criteria.where
import io.github.partialize.persistence.QueryStorage
import io.github.partialize.persistence.SimpleQueryStorage
import io.github.partialize.ulid.ULID
import io.github.partialize.user.entity.UserData
import io.github.partialize.user.repository.UserRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component

@Component
class UserStorage(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val usersMapper: UsersMapper,
) : QueryStorage<User, ULID> by SimpleQueryStorage(
    userRepository,
    { userMapper.map(it) },
    { usersMapper.map(it) }
) {
    suspend fun load(name: String): User? {
        return load(where(UserData::name).`is`(name))
    }
}

suspend fun UserStorage.loadOrFail(name: String): User {
    return load(name) ?: throw EmptyResultDataAccessException(1)
}
