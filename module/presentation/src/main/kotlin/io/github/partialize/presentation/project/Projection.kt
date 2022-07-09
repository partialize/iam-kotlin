package io.github.partialize.presentation.project

data class Projection<T : Any>(
    val value: T,
    val node: ProjectNode
)
