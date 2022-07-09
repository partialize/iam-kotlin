package io.github.partialize.client.entity

import io.github.partialize.ulid.ULID

interface ClientEntity {
    val clientId: ULID?
}
