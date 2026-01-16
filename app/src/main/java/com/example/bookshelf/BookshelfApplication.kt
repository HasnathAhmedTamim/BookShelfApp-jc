package com.example.bookshelf

import android.app.Application
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.data.NetworkBooksRepository

class BookshelfApplication : Application() {
    // simple container
    val booksRepository: BooksRepository by lazy {
        NetworkBooksRepository()
    }
}
