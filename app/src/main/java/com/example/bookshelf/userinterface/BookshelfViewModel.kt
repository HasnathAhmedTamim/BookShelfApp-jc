package com.example.bookshelf.userinterface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.BooksRepository
import kotlinx.coroutines.launch

class BookshelfViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    var uiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

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
                    uiState = BookshelfUiState.Success(books)
                }
            } catch (e: Exception) {
                uiState = BookshelfUiState.Error
            }
        }
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