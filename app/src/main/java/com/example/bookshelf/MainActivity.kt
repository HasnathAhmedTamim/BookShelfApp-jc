//kotlin
package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.bookshelf.userinterface.BookshelfApp
import com.example.bookshelf.userinterface.BookshelfViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BookshelfViewModel by viewModels {
        val app = application as BookshelfApplication
        BookshelfViewModel.provideFactory(app.booksRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BookshelfApp(
                    uiState = viewModel.uiState,
                    onRetry = { viewModel.loadBooks("jazz+history") },
                    onBookClick = { book -> viewModel.onBookSelected(book) },
                    onBack = { viewModel.onBackToList() }
                )
            }
        }
    }
}
