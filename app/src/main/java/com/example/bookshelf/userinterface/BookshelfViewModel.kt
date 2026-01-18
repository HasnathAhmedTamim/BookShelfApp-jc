
package com.example.bookshelf.userinterface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BooksRepository
import kotlinx.coroutines.launch

class BookshelfViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    var uiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    // cached list used to restore the grid when navigating back
    private var cachedBooks: List<Book> = emptyList()

    init {
        // Default search term
        loadBooks("jazz+history")
    }

    fun loadBooks(query: String) {
        uiState = BookshelfUiState.Loading
        viewModelScope.launch {
            try {
                val books = repository.getBooks(query)
                if (books.isEmpty()) {
                    uiState = BookshelfUiState.Error
                } else {
                    // cache the loaded list so we can restore it on back
                    cachedBooks = books
                    uiState = BookshelfUiState.ListSuccess(books)
                }
            } catch (e: Exception) {
                uiState = BookshelfUiState.Error
            }
        }
    }

    fun onBookSelected(book: Book) {
        uiState = BookshelfUiState.DetailSuccess(book)
    }

    fun onBackToList() {
        uiState = BookshelfUiState.ListSuccess(cachedBooks)
    }

    companion object {
        fun provideFactory(
            repository: BooksRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(BookshelfViewModel::class.java)) {
                    return BookshelfViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
