package com.example.bookshelf.userinterface

import com.example.bookshelf.data.Book

sealed interface BookshelfUiState {
    object Loading : BookshelfUiState
    data class Success(val books: List<Book>) : BookshelfUiState
    object Error : BookshelfUiState
}
