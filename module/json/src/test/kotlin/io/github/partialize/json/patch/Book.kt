package io.github.partialize.json.patch

data class Book(
    var title: String,
    var author: Author,
    var phoneNumber: String?,
    var tags: MutableList<String>,
    var content: String
)
