// kotlin
package com.example.bookshelf.userinterface

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookshelfApp(
    viewModel: BookshelfViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    val recentSearches = viewModel.recentSearches

    // Handle system back button when on detail screen
    BackHandler(enabled = uiState is BookshelfUiState.DetailSuccess) {
        viewModel.onBackToList()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is BookshelfUiState.Loading -> LoadingScreen()
            is BookshelfUiState.Error -> ErrorScreen(
                onRetry = { viewModel.performSearch() }
            )
            is BookshelfUiState.ListSuccess -> HomeScreen(
                books = uiState.books,
                recentSearches = recentSearches,
                searchQuery = viewModel.searchQuery,
                onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                onSearch = { viewModel.performSearch() },
                onClearSearch = { viewModel.clearSearch() },
                onRecentSearchClick = { viewModel.performSearch(it) },
                onBookClick = { viewModel.onBookSelected(it) },
                modifier = modifier
            )
            is BookshelfUiState.DetailSuccess -> BookDetailScreen(
                book = uiState.book,
                onBack = { viewModel.onBackToList() }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text("Loading books...")
        }
    }
}

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Failed to load books")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
@Suppress("unused")
fun EmptySearchScreen(searchQuery: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📚",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No books found",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (searchQuery.isNotEmpty())
                    "No results for \"$searchQuery\""
                else
                    "Try searching for a book",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}

