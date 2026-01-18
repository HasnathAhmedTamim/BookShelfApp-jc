# Bookshelf 📚

> A modern Android book discovery app with smart search, elegant Material3 UI, and seamless navigation

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-1.5+-green.svg)](https://developer.android.com/jetpack/compose)
[![Material3](https://img.shields.io/badge/Material3-Latest-blue.svg)](https://m3.material.io/)
[![API](https://img.shields.io/badge/Google_Books_API-v1-orange.svg)](https://developers.google.com/books)

## Overview
An Android book discovery app built with Kotlin and Jetpack Compose. Search for books, browse them in a grid, and view detailed information including cover, title, authors, description, ratings, and more. Features a powerful search system with recent searches, always-visible home navigation, and a polished Material3 UI. Uses Google Books API for book data and MVVM architecture with Compose state management.

## Screenshots

### Home Screen
- Popular books grid on app launch
- Always-visible home button (🏠)
- Smart search bar with search button
- Recent searches chips

### Search Results
- Adaptive grid layout
- Consistent card sizing (6dp elevation)
- Loading placeholders during image load
- Book metadata preview

### Book Details
- Large cover image (300dp)
- Complete book information
- Star ratings and review counts
- Scrollable description
- Material3 TopAppBar with back navigation

### Empty & Error States
- Friendly "No books found" message
- Retry functionality
- Clear navigation options

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

## How It Works (User Flow)

### 1. App Launch & Home State
1. User opens app
2. `MainActivity` creates `BookshelfViewModel` with DI-provided `BooksRepository`
3. ViewModel's `init` block calls `loadBooks("popular books")`
4. Loading state shows briefly
5. Home screen displays with popular books grid

### 2. Search Flow
1. User types query in search bar (e.g., "kotlin")
2. User clicks **[Search]** button or presses Enter
3. `viewModel.performSearch()` is called
4. Query saved to recent searches (max 5)
5. API request to Google Books
6. Results displayed in grid
7. Recent searches chip appears for quick re-search

### 3. Navigation to Book Details
1. User taps a book card in grid
2. `viewModel.onBookSelected(book)` sets `DetailSuccess` state
3. `BookDetailScreen` displays with:
   - Large cover image
   - Title, authors, ratings
   - Publication info, page count
   - Categories
   - Full description (scrollable)
4. Back arrow in TopAppBar and system back button available

### 4. Return to Home (Multiple Ways)
- **Option A**: Click 🏠 home button → `clearSearch()` → loads "popular books"
- **Option B**: Click ✕ clear button → same as home
- **Option C**: From detail screen, press back → `onBackToList()` → restores cached list
- **Option D**: System back button → `BackHandler` intercepts → returns to list

### 5. Handle Empty Results
1. Search returns no books
2. `EmptySearchScreen` shows "📚 No books found"
3. User can:
   - Click **[Try Again]** to retry search
   - Click 🏠 or ✕ to return home
   - Type new search query

### 6. State Preservation
- Book list cached in ViewModel
- No API reload when navigating back from details
- Recent searches persist across navigations
- Search query preserved in UI

## Key Files

**Core App:**
- `app/src/main/java/com/example/bookshelf/MainActivity.kt` – Entry point, connects ViewModel to UI
- `app/src/main/java/com/example/bookshelf/BookshelfApplication.kt` – App-level DI, provides BooksRepository

**ViewModel & State:**
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfViewModel.kt` – State management, search logic, navigation, caching
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfUiState.kt` – Sealed UI state interface

**UI Composables:**
- `app/src/main/java/com/example/bookshelf/userinterface/BookshelfScreen.kt` – Main app routing, BackHandler, LoadingScreen, ErrorScreen, EmptySearchScreen
- `app/src/main/java/com/example/bookshelf/userinterface/HomeScreen.kt` – Combines SearchBar + RecentSearches + BooksGrid
- `app/src/main/java/com/example/bookshelf/userinterface/SearchBar.kt` – Search input with home button, clear, and search button + RecentSearchesSection
- `app/src/main/java/com/example/bookshelf/userinterface/BooksGrid.kt` – Grid list with BookItem cards (optimized with key, loading/error states)
- `app/src/main/java/com/example/bookshelf/userinterface/BookDetailScreen.kt` – Detail view with scrollable layout, complete book info
- `app/src/main/java/com/example/bookshelf/ui/theme/*` – Material3 theme configuration

**Data & Network:**
- `app/src/main/java/com/example/bookshelf/data/Book.kt` – Enhanced data model (id, title, description, authors, thumbnailUrl, rating, ratingsCount, publishedDate, pageCount, categories)
- `app/src/main/java/com/example/bookshelf/data/BooksRepository.kt` – Repository interface & NetworkBooksRepository implementation
- `app/src/main/java/com/example/bookshelf/network/BooksApiService.kt` – Retrofit API service for Google Books API
- `app/src/main/java/com/example/bookshelf/network/model/SearchResponse.kt` – DTOs: SearchResponse, VolumeItem, VolumeInfo
- `app/src/main/java/com/example/bookshelf/network/model/VolumeDetailsResponse.kt` – DTOs: VolumeDetailsResponse, VolumeInfoDetails, ImageLinks

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

### Recommended Test Scenarios

**Unit Tests (ViewModel):**
- Search query updates and validation
- Recent searches management (max 5, deduplication)
- State transitions (Loading → ListSuccess → DetailSuccess)
- Cache management on back navigation
- Error handling with network failures

**UI Tests (Compose):**
- Search bar input and button clicks
- Home button navigation
- Book card clicks and navigation to details
- Recent searches chip interactions
- Empty state display when no results
- Loading state visibility
- Error screen with retry functionality
- System back button behavior

**Integration Tests:**
- End-to-end search flow
- API response parsing and mapping
- Image loading with Coil
- State preservation across navigation

**Tools:**
- `androidx.compose.ui.test` for Compose UI testing
- JUnit for unit tests
- Mockito or MockK for mocking repository

## Notes & Tips
- Default search query: "popular books" (loads on app start and when clearing search)
- Recent searches: Up to 5 unique searches maintained
- Home button (🏠): Always visible, returns to popular books home page
- Clear button (✕): Clears search text and returns to home
- Google Books API: Fetches title, authors, description, ratings, categories, page count, publication date
- Image URLs: Automatically converted from HTTP to HTTPS
- Navigation: System back button handled via `BackHandler` composable
- State caching: Book list cached to avoid unnecessary API calls on back navigation
- Empty results: Friendly message with retry option
- Inspect `BookshelfViewModel.provideFactory(...)` to adjust DI for tests

## Future Enhancements
- **Advanced Filtering**: Filter by genre, publication date, rating, language
- **Sorting Options**: Sort by relevance, newest, rating, title
- **Favorites/Bookmarks**: Save favorite books to local database (Room DB)
- **Reading List**: Create and manage custom reading lists
- **Book Preview**: Open external links to full book previews on Google Books
- **Pagination**: Implement infinite scroll or pagination for large result sets
- **Offline Support**: Cache book data for offline browsing
- **Dark Mode**: Enhanced dark theme customization
- **Share Books**: Share book details via social media or messaging
- **Book Reviews**: Display user reviews from Google Books


## Contributing
- Open an issue for bugs or feature requests.
- Create PRs targeting the `main` branch.

## License
Specify project license (e.g. MIT). Include a `LICENSE` file.
