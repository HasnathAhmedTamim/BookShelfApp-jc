// kotlin
package com.example.bookshelf.data

data class Book(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val description: String = "No description available",
    val authors: List<String> = emptyList()
)
