package com.example.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookshelf.ui.theme.BookshelfTheme
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
                    onRetry = { viewModel.loadBooks("jazz+history") }
                )
            }
        }
    }
}