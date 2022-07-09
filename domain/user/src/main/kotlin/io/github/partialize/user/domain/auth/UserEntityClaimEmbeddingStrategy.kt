package io.github.partialize.user.domain.auth

import io.github.partialize.auth.domain.authorization.ClaimMapping
import io.github.partialize.auth.domain.token.ClaimEmbeddingStrategy
import io.github.partialize.user.entity.UserEntity
import org.springframework.stereotype.Component

@Component
@ClaimMapping(UserEntity::class)
class UserEntityClaimEmbeddingStrategy : ClaimEmbeddingStrategy<UserEntity> {
    override val clazz = UserEntity::class

    override suspend fun embedding(principal: UserEntity): Map<String, Any> {
        val claims = mutableMapOf<String, Any>()

        val userId = principal.userId
        if (userId != null) {
            claims["uid"] = userId
        }

        return claims
    }
}
