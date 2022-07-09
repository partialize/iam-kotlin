package io.github.partialize.data.annotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Key(
    val name: String = ""
)
