package io.github.partialize.data

import io.github.partialize.data.annotation.GeneratedValue
import java.time.Instant

interface Creatable {
    @GeneratedValue
    var createdAt: Instant?
}
