package io.github.partialize.user.domain

import io.github.partialize.auth.domain.hash
import io.github.partialize.event.EventPublisher
import io.github.partialize.persistence.Persistence
import io.github.partialize.persistence.proxy
import io.github.partialize.persistence.proxyNotNull
import io.github.partialize.user.entity.UserCredentialData
import io.github.partialize.user.repository.UserCredentialRepository
import java.security.MessageDigest

class UserCredential(
    value: UserCredentialData,
    userCredentialRepository: UserCredentialRepository,
    eventPublisher: EventPublisher
) : Persistence<UserCredentialData, Long>(
    value,
    userCredentialRepository,
    eventPublisher = eventPublisher
) {
    val id by proxyNotNull(root, UserCredentialData::id)
    val userId by proxy(root, UserCredentialData::userId)

    private val hashAlgorithm by proxy(root, UserCredentialData::hashAlgorithm)

    fun isPassword(password: String): Boolean {
        return root[UserCredentialData::password] == encodePassword(password)
    }

    fun setPassword(password: String) {
        root[UserCredentialData::password] = encodePassword(password)
    }

    private fun encodePassword(password: String): String {
        val messageDigest = MessageDigest.getInstance(hashAlgorithm)
        return messageDigest.hash(password)
    }
}
