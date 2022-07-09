package io.github.partialize.persistence.domain

import io.github.partialize.data.repository.Repository
import io.github.partialize.persistence.Persistence
import io.github.partialize.persistence.entity.PersonData
import io.github.partialize.persistence.proxy
import io.github.partialize.ulid.ULID

class Person(
    value: PersonData,
    repository: Repository<PersonData, ULID>
) : Persistence<PersonData, ULID>(value, repository) {
    val id by proxy(root, PersonData::id)
    var name by proxy(root, PersonData::name)
    var age by proxy(root, PersonData::age)
}
