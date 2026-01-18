// kotlin
package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BooksRepository {
    suspend fun getBooks(query: String): List<Book>
}

class NetworkBooksRepository : BooksRepository {
    override suspend fun getBooks(query: String): List<Book> =
        withContext(Dispatchers.IO) {
            val searchResponse = BooksApi.service.searchBooks(query)
            val items = searchResponse.items ?: emptyList()

            items.mapNotNull { item ->
                val info = item.volumeInfo ?: return@mapNotNull null

                val rawThumb = info.imageLinks?.thumbnail
                val thumbnailUrl = rawThumb
                    ?.replace("http://", "https://")
                    ?: ""

                Book(
                    id = item.id,
                    title = info.title ?: "No title",
                    thumbnailUrl = thumbnailUrl,
                    description = info.description ?: "No description available",
                    authors = info.authors ?: emptyList()
                )
            }
        }
}
