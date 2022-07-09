package io.github.partialize.data.repository.in_memory

import io.github.partialize.data.repository.Repository

interface InMemoryRepository<T : Any, ID : Any> : Repository<T, ID>
