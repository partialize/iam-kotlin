package io.github.partialize.data

import io.github.partialize.data.annotation.GeneratedValue
import java.time.Instant

abstract class ModifiableULIDEntity : ULIDEntity(), Modifiable {
    @GeneratedValue
    override var createdAt: Instant? = null

    @GeneratedValue
    override var updatedAt: Instant? = null
}
