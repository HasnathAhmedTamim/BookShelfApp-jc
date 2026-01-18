// kotlin
package com.example.bookshelf.data

data class Book(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val description: String = "No description available",
    val authors: List<String> = emptyList(),
    val rating: Double? = null,
    val ratingsCount: Int? = null,
    val publishedDate: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null
)
