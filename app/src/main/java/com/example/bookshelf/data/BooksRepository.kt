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

            val books = mutableListOf<Book>()

            // এক coroutine এর ভিতরে sequentially details fetch
            for (item in items) {
                val volumeId = item.id
                try {
                    val details = BooksApi.service.getBookDetails(volumeId)
                    val title = details.volumeInfo?.title ?: "No title"
                    val rawThumb = details.volumeInfo?.imageLinks?.thumbnail

                    // http → https
                    val thumbnailUrl = rawThumb
                        ?.replace("http://", "https://")
                        ?: ""

                    books.add(
                        Book(
                            id = volumeId,
                            title = title,
                            thumbnailUrl = thumbnailUrl
                        )
                    )
                } catch (e: Exception) {
                    // কোনো একটা book fail করলে, ওটাকে skip করো, বাকি গুলো load হোক
                }
            }
            books
        }
}