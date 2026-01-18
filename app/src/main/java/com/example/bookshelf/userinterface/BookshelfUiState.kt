package com.example.bookshelf.userinterface

import com.example.bookshelf.data.Book

sealed interface BookshelfUiState {
    object Loading : BookshelfUiState
    data class ListSuccess(val books: List<Book>) : BookshelfUiState
    data class DetailSuccess(val book: Book) : BookshelfUiState
    object Error : BookshelfUiState
}