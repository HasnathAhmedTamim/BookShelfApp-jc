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

### User Flow:
1. **App Launch**: `MainActivity` obtains `BookshelfViewModel` via `viewModels` factory with DI-provided `BooksRepository`.
2. **Initial Load**: ViewModel's `init { loadBooks("jazz+history") }` fetches books and sets state to `ListSuccess(books)`.
3. **Display Grid**: `BookshelfApp` observes `uiState` and renders `BooksGrid` with cached book list.
4. **Book Selection**: User taps a book card → `onBookClick(book)` calls `viewModel.onBookSelected(book)`.
5. **Detail Screen**: ViewModel sets state to `DetailSuccess(book)` → `BookDetailScreen` displays full book info (cover, title, authors, description).
6. **Back Navigation**: User taps back button → `onBackToList()` restores cached list via `ListSuccess(cachedBooks)` → grid reappears.
7. **Error/Retry**: If network fails, `uiState = Error` → user taps "Retry" → `viewModel.loadBooks()` re-attempts fetch.

### State Management:
- ViewModel caches the loaded book list so returning from details avoids re-fetching.
- `uiState` drives all UI rendering; compose automatically recomposes when state changes.
- Loading/error/list/detail states are exhaustively handled in `BookshelfApp` when expression.

## Key files
**Core App:**
- `app/src/main/java/com/example/bookshelf/MainActivity.kt` – Entry point, connects ViewModel to UI
- `app/src/main/java/com/example/bookshelf/BookshelfApplication.kt` – App-level DI, provides BooksRepository

**ViewModel & State:**
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfViewModel.kt` – State management, navigation logic, caching
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfUiState.kt` – Sealed UI state interface

**UI Composables:**
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfScreen.kt` – Main app routing (shows correct screen per state)
- `app/src/main/java/com/example/bookshelf/userinterface/BooksGrid.kt` – Grid list with `BookItem` cards (optimized with key, loading/error states)
- `app/src/main/java/com/example/bookshelf/userinterface/BookDetailScreen.kt` – Detail view with scrollable layout
- `app/src/main/java/com/example/bookshelf/ui/theme/*` – Material3 theme configuration

**Data & Network:**
- `app/src/main/java/com/example/bookshelf/data/Book.kt` – Data model (id, title, description, authors, thumbnailUrl)
- `app/src/main/java/com/example/bookshelf/data/BooksRepository.kt` – Repository interface & NetworkBooksRepository implementation
- `app/src/main/java/com/example/bookshelf/network/BooksApiService.kt` – Retrofit API service for Google Books API
- `app/src/main/java/com/example/bookshelf/network/model/SearchResponse.kt` – DTO for search results
- `app/src/main/java/com/example/bookshelf/network/model/VolumeDetailsResponse.kt` – DTO for book details

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

## Improvements & Future Enhancements
- **Search Functionality**: Add a search bar allowing users to enter custom queries
- **Favorites/Bookmarks**: Save favorite books to local database (Room DB)
- **Book Preview**: Open external links to full book previews on Google Books
- **Pagination**: Implement LazyColumn/LazyVerticalGrid pagination for large result sets
- **Caching**: Add local caching layer with Room or DataStore to avoid repeated API calls
- **Reading List**: Allow users to create and manage reading lists
- **Book Reviews/Ratings**: Integrate user ratings or external review APIs
- **Offline Support**: Download book metadata for offline browsing
- **Advanced Filtering**: Filter by genre, publication date, author
- **Dark Mode**: Enhanced dark theme support for Material3
- **Animations**: Add transition animations between list and detail screens

## Contributing
- Open an issue for bugs or feature requests.
- Create PRs targeting the `main` branch.

## License
Specify project license (e.g. MIT). Include a `LICENSE` file.
