package io.github.partialize.data

import io.github.partialize.data.annotation.GeneratedValue
import java.time.Instant

interface Updatable {
    @GeneratedValue
    var updatedAt: Instant?
}
