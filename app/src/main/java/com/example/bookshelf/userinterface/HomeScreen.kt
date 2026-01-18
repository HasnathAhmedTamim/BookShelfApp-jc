package com.example.bookshelf.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookshelf.data.Book

@Composable
fun HomeScreen(
    books: List<Book>,
    recentSearches: List<String>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClearSearch: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearch = onSearch,
            onClearSearch = onClearSearch
        )
        RecentSearchesSection(
            recentSearches = recentSearches,
            onSearchClick = onRecentSearchClick
        )

        // Show empty state if no books, otherwise show grid
        if (books.isEmpty()) {
            EmptySearchScreen(
                searchQuery = searchQuery,
                onRetry = onSearch
            )
        } else {
            BooksGrid(
                books = books,
                onBookClick = onBookClick
            )
        }
    }
}

