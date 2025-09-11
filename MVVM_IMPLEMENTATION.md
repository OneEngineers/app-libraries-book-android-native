# MVVM Implementation for Library Lighthouse App

This document describes the MVVM (Model-View-ViewModel) architecture implementation for the Library Lighthouse Android app with Jetpack Compose.

## Architecture Overview

The app follows the MVVM pattern with the following components:

### 1. Model Layer
- **Data Models**: `User.kt`, `LoginRequest.kt`, `RegisterRequest.kt`, `AuthResponse.kt`
- **Repository**: `AuthRepository.kt` - Handles data operations and business logic

### 2. View Layer
- **Compose Screens**: `LoginScreen.kt`, `RegisterScreen.kt`, `HomeScreen.kt`
- **Navigation**: `MyAppNavigation.kt` - Handles screen navigation

### 3. ViewModel Layer
- **LoginViewModel**: Manages login screen state and business logic
- **RegisterViewModel**: Manages registration screen state and business logic

## Key Features Implemented

### App Flow
1. **Home Screen**: Welcome screen with options to login or register
2. **Login Screen**: Email/password authentication with validation
3. **Register Screen**: User registration with form validation
4. **Main Screen**: Library dashboard (accessed after successful authentication)
5. **Profile Screen**: User profile with account information and settings access
6. **Settings Screen**: App preferences and account settings
7. **Search Screen**: Book search with categories and recent searches
8. **Book Details Screen**: Detailed book information with borrow/wishlist options

### MVVM Benefits
- **Separation of Concerns**: UI logic separated from business logic
- **State Management**: Reactive UI updates using StateFlow
- **Testability**: ViewModels can be easily unit tested
- **Dependency Injection**: Using Hilt for clean dependency management

## Technical Implementation

### Dependencies Added
```kotlin
// MVVM dependencies
implementation(libs.lifecycle.viewmodel.compose)
implementation(libs.lifecycle.runtime.compose)

// Hilt dependencies
implementation(libs.hilt.android)
kapt(libs.hilt.android.compiler)
```

### State Management
- Uses `StateFlow` for reactive state management
- UI state is collected using `collectAsStateWithLifecycle()`
- Loading states, error handling, and success states are properly managed

### Navigation
- Jetpack Navigation Compose for screen navigation
- Proper back stack management
- Navigation callbacks for screen transitions

### Form Validation
- Real-time validation in ViewModels
- Error message display in UI
- Input field validation (email format, password length, etc.)

## File Structure

```
app/src/main/java/com/assistant/libraries/
├── data/
│   ├── model/
│   │   └── User.kt
│   └── repository/
│       └── AuthRepository.kt
├── presentation/
│   ├── viewmodel/
│   │   ├── LoginViewModel.kt
│   │   └── RegisterViewModel.kt
│   └── views/
│       ├── LoginScreen.kt
│       ├── RegisterScreen.kt
│       ├── HomeScreen.kt
│       ├── MainScreen.kt
│       ├── MainActivity.kt
│       ├── MyAppNavigation.kt
│       ├── ProfileScreen.kt
│       ├── SettingsScreen.kt
│       ├── SearchScreen.kt
│       ├── BookDetailsScreen.kt
│       └── Routes.kt
```

## Usage

### Running the App
1. The app starts with the Home screen
2. Users can choose to "Create an Account" or "Login"
3. After successful authentication, users are navigated to the Main screen
4. From the Main screen, users can:
   - Access their profile via the profile icon
   - Search for books (placeholder for now)
   - View book details by clicking on books
   - Navigate to settings from the profile screen
5. All screens follow MVVM pattern with proper state management

### Testing
- ViewModels can be unit tested independently
- Repository can be mocked for testing
- UI components can be tested with Compose testing framework

## Future Enhancements

1. **Real API Integration**: Replace mock data with actual API calls
2. **Data Persistence**: Add Room database for local data storage
3. **Error Handling**: Implement comprehensive error handling strategies
4. **Loading States**: Add skeleton loading screens
5. **Form Validation**: Add more sophisticated validation rules
6. **Security**: Implement proper token management and secure storage

## Best Practices Followed

1. **Single Responsibility**: Each class has a single responsibility
2. **Dependency Injection**: Using Hilt for clean architecture
3. **Reactive Programming**: Using StateFlow for state management
4. **Compose Best Practices**: Proper state hoisting and composition
5. **Navigation**: Proper navigation patterns with back stack management
6. **Error Handling**: User-friendly error messages and loading states

This implementation provides a solid foundation for a scalable Android app using modern Android development practices.
