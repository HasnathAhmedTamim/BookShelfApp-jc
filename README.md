# Bookshelf

## Overview
An Android book discovery app built with Kotlin and Jetpack Compose. Search for books, browse them in a grid, and view detailed information including cover, title, authors, and description. Uses Google Books API for book data and MVVM architecture with Compose state management.

## Implemented Features
- **Book Grid**: Browse books in an adaptive grid with consistent card sizing and image cropping
- **Book Details Screen**: Tap any book to view full details (cover, title, authors, description)
- **Navigation**: Seamless transitions between grid list and detail screens with cached list state
- **Loading/Error States**: Circular progress indicator while loading, error screen with retry button
- **Image Handling**: Loading placeholders and error fallbacks for book covers using SubcomposeAsyncImage
- **Jetpack Compose UI**: Full Material3 theming with responsive layouts
- **ViewModel State Management**: Sealed interface `BookshelfUiState` with `Loading`, `ListSuccess`, `DetailSuccess`, and `Error` states
- **Google Books API Integration**: Fetches book data (title, description, authors, thumbnail) from network
- **Performance Optimization**: Grid items keyed by book ID for efficient recomposition
- **Accessibility**: Proper semantics, content descriptions, and Material3 ripple effects

## Architecture
- **MVVM Pattern**:
    - `Book` data class: model with id, title, thumbnailUrl, description, authors
    - `BooksRepository` interface: abstracts data access (network)
    - `NetworkBooksRepository`: fetches books from Google Books API, maps to `Book` model
    - `BookshelfViewModel`: manages UI state, handles book selection/navigation, caches list
    - Composable screens: `BookshelfApp`, `BooksGrid`, `BookDetailScreen`, `LoadingScreen`, `ErrorScreen`

- **UI State (Sealed Interface)**:
    ```kotlin
    sealed interface BookshelfUiState {
        object Loading : BookshelfUiState
        data class ListSuccess(val books: List<Book>) : BookshelfUiState
        data class DetailSuccess(val book: Book) : BookshelfUiState
        object Error : BookshelfUiState
    }
    ```

- **Dependency Injection**: `BookshelfApplication` provides `BooksRepository` via factory for `BookshelfViewModel`

## How it works (flow)
1. App launches and `MainActivity` obtains `BookshelfViewModel` via `viewModels` factory.
2. `MainActivity` calls `setContent { BookshelfApp(uiState = viewModel.uiState, onRetry = { viewModel.loadBooks("jazz+history") }) }`.
3. UI observes `uiState` from the ViewModel and displays data, loading, or error states.
4. On retry, ViewModel reloads books (example query: `jazz+history`) from the repository.

## Key files
- `app/src/main/java/com/example/bookshelf/MainActivity.kt`
- `app/src/main/java/com/example/bookshelf/BookshelfApplication.kt`
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfViewModel.kt`
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfApp.kt`
- `app/src/main/java/com/example/bookshelf/ui/theme/*`

## Requirements
- Android Studio (compatible with Android Studio Otter)
- JDK 11+ (or project configured JDK)
- Gradle (wrapper included)

## Setup & Run
1. Clone the repo.
2. Open project in Android Studio.
3. Build and run on an emulator or device.
4. Or from terminal (Windows):
    - `.\gradlew assembleDebug`
    - `.\gradlew installDebug`

## Testing
- Add unit tests for ViewModel logic (mock repository).
- UI tests for Compose screens using `androidx.compose.ui.test`.

## Notes & Tips
- The example query used by the app is `jazz+history` when retrying load.
- Inspect `BookshelfViewModel.provideFactory(...)` to adjust DI for tests.


## Contributing
- Open an issue for bugs or feature requests.
- Create PRs targeting the `main` branch.

## License
Specify project license (e.g. MIT). Include a `LICENSE` file.
