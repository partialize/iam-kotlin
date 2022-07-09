package io.github.partialize.data.cache

interface NestedQueryStorage<T : Any> : QueryStorage<T>, GeneralNestedStorage<NestedQueryStorage<T>> {
    suspend fun checkout(): Set<Pair<SelectQuery, Collection<T>>>
}
