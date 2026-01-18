# Bookshelf

## Overview
An Android book discovery app built with Kotlin and Jetpack Compose. Search for books, browse them in a grid, and view detailed information including cover, title, authors, description, ratings, and more. Features a powerful search system with recent searches, always-visible home navigation, and a polished Material3 UI. Uses Google Books API for book data and MVVM architecture with Compose state management.

## Implemented Features

### 🔍 Search & Navigation
- **Smart Search Bar**: Search books with text input + dedicated search button, keyboard Enter support
- **Home Button**: Always-visible 🏠 home icon for instant return to popular books
- **Recent Searches**: Quick-access chips showing last 5 searches with icons
- **Clear Search**: X button clears search and returns to home page
- **Default Home State**: App loads "popular books" on start and when clearing search

### 📚 Book Browsing & Display
- **Adaptive Grid**: Responsive grid layout with consistent card sizing and elevation
- **Book Cards**: Elevated cards (6dp) with rounded corners, Material ripple effects, and fixed dimensions
- **Image Handling**: Loading spinners, error fallbacks ("No Image"), and HTTPS image conversion
- **Book Metadata**: Displays title, authors, rating, page count, publication date, categories
- **Performance Optimized**: Grid items keyed by book ID for efficient recomposition

### 📖 Book Details Screen
- **Enhanced Layout**: Large cover image (300dp), comprehensive book information
- **Detailed Information**: Title, authors, publication date, page count, star ratings, categories, full description
- **Styled UI**: Material3 TopAppBar with primary color, scrollable content, proper spacing
- **Navigation**: Back arrow button + system back button support via BackHandler

### 🎨 UI/UX Enhancements
- **Loading States**: Circular progress indicator with "Loading books..." message
- **Empty State**: Friendly "📚 No books found" screen with retry button
- **Error Handling**: Error screen with retry functionality
- **Material3 Design**: Full Material3 theming, responsive layouts, proper color schemes
- **Visual Feedback**: Card ripples on click, loading placeholders, smooth transitions
- **Accessibility**: Proper content descriptions, AutoMirrored icons for RTL support

### 🏗️ Architecture & State Management
- **MVVM Pattern**: Clean separation of concerns with ViewModel, Repository, and UI layers
- **State Management**: Sealed interface `BookshelfUiState` with Loading, ListSuccess, DetailSuccess, Error states
- **Cached Data**: Book lists cached in ViewModel to avoid unnecessary API calls on navigation
- **Recent Searches**: Maintained across sessions (up to 5 unique searches)

## Architecture

### MVVM Pattern
- **Data Layer**:
    - `Book` data class: Enhanced model with id, title, thumbnailUrl, description, authors, rating, ratingsCount, publishedDate, pageCount, categories
    - `BooksRepository` interface: Abstracts data access (network)
    - `NetworkBooksRepository`: Fetches books from Google Books API, maps JSON to Book model with Gson
    - Network DTOs: `SearchResponse`, `VolumeItem`, `VolumeInfo`, `ImageLinks`

- **ViewModel Layer**:
    - `BookshelfViewModel`: Manages UI state, handles book selection/navigation, caches list, manages search
    - `searchQuery`: Current search text state
    - `recentSearches`: List of last 5 searches
    - `cachedBooks`: Preserves book list for back navigation
    - Functions: `performSearch()`, `clearSearch()`, `updateSearchQuery()`, `loadBooks()`, `onBookSelected()`, `onBackToList()`

- **UI Layer**:
    - `BookshelfApp`: Main app router, handles BackHandler for system back button
    - `HomeScreen`: Combines SearchBar + RecentSearches + BooksGrid, handles empty state
    - `SearchBar`: Search input with home button, clear button, and search button
    - `BooksGrid`: Adaptive grid with BookItem cards, keyed items for performance
    - `BookDetailScreen`: Detailed book view with Material3 TopAppBar
    - `LoadingScreen`: Progress indicator during API calls
    - `ErrorScreen`: Error message with retry button
    - `EmptySearchScreen`: Friendly no-results state with emoji and retry

### UI State Management (Sealed Interface)
```kotlin
sealed interface BookshelfUiState {
    object Loading : BookshelfUiState
    data class ListSuccess(val books: List<Book>) : BookshelfUiState
    data class DetailSuccess(val book: Book) : BookshelfUiState
    object Error : BookshelfUiState
}
```

### Dependency Injection
- `BookshelfApplication` provides `BooksRepository` via ViewModelProvider.Factory
- `MainActivity` obtains `BookshelfViewModel` using `by viewModels` with custom factory

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
