package io.github.partialize.data

import io.github.partialize.data.annotation.GeneratedValue
import io.github.partialize.ulid.ULID
import org.springframework.data.annotation.Id

abstract class ULIDEntity : Entity<ULID>() {
    @Id
    @GeneratedValue
    override var id: ULID = ULID.randomULID()
}
