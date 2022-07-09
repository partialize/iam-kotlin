package io.github.partialize.user.entity

import io.github.partialize.ulid.ULID

interface UserEntity {
    val userId: ULID?
}
