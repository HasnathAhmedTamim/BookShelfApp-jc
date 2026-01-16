# Bookshelf

## Overview
Simple Android app written in Kotlin (Jetpack Compose) with a ViewModel-driven UI. Loads book data from a repository and displays it in a Compose screen. Built with Gradle.

## Implemented
- Jetpack Compose UI (`BookshelfApp`)
- `BookshelfViewModel` with UI state and retry/load logic
- `BookshelfApplication` providing a `booksRepository`
- Edge-to-edge support and Compose Material3 theming
- Basic error/retry flow for loading books

## Architecture
- MVVM pattern:
    - Repository: data access (network / local)
    - ViewModel: exposes `uiState` and load/retry methods
    - Composable screens: observe `uiState` and render UI
- Uses DI via `BookshelfApplication` to provide repository factory for `BookshelfViewModel`.

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
