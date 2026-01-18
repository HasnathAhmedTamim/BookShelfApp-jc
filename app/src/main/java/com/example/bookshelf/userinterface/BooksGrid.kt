//kotlin
package com.example.bookshelf.userinterface

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import coil.compose.SubcomposeAsyncImage
import com.example.bookshelf.data.Book

// Spacing constants for consistency
private val GRID_SPACING = 8.dp
private val CARD_PADDING = 8.dp
private val ITEM_SPACING = 8.dp
private val CARD_HEIGHT = 240.dp
private val IMAGE_HEIGHT = 140.dp
private val CARD_ELEVATION = 6.dp  // Increased from 4dp for better visibility
private val CORNER_RADIUS = 8.dp
private val IMAGE_CORNER_RADIUS = 6.dp

@Composable
fun BooksGrid(
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(GRID_SPACING),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING)
    ) {
        items(books, key = { it.id }) { book ->
            BookItem(book = book, onClick = { onBookClick(book) })
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(CARD_HEIGHT)
            .clickable(
                interactionSource = interactionSource,
                onClick = onClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = CARD_ELEVATION),
        shape = RoundedCornerShape(CORNER_RADIUS)
    ) {
        Column(
            modifier = Modifier.padding(CARD_PADDING),
            horizontalAlignment = Alignment.Start
        ) {
            // Image with loading and error states
            SubcomposeAsyncImage(
                model = book.thumbnailUrl,
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IMAGE_HEIGHT)
                    .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(30.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Image",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }
            )

            Spacer(Modifier.height(ITEM_SPACING))

            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = book.authors.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "Unknown",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }
    }
}
