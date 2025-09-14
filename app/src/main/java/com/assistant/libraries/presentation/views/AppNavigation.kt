package com.assistant.libraries.presentation.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.assistant.libraries.presentation.views.auth.LoginScreen
import com.assistant.libraries.presentation.views.auth.RegisterScreen
import com.assistant.libraries.presentation.views.books.BookDetailsScreen
import com.assistant.libraries.presentation.views.feature.SearchScreen
import com.assistant.libraries.presentation.views.home.HomeScreen
import com.assistant.libraries.presentation.views.users.ProfileScreen
import com.assistant.libraries.presentation.views.users.SettingsScreen

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable(Routes.HomeScreen) {
            HomeScreen(
                onCreateAccountClick = {
                    navController.navigate(Routes.SignupScreen)
                },
                onLoginClick = {
                    navController.navigate(Routes.LoginScreen)
                }
            )
        }

        composable(Routes.LoginScreen) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(Routes.HomeScreen) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.SignupScreen)
                }
            )
        }

        composable(Routes.SignupScreen) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(Routes.HomeScreen) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LoginScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MainScreen) {
            LibraryLighthouseScreen(
                onProfileClick = { navController.navigate(Routes.ProfileScreen) },
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                onBookClick = { bookId -> 
                    navController.navigate("${Routes.BookDetailsScreen}/$bookId")
                }
            )
        }

        composable(Routes.ProfileScreen) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.HomeScreen) { inclusive = true }
                    }
                },
                onSettingsClick = { navController.navigate(Routes.SettingsScreen) }
            )
        }

        composable(Routes.SettingsScreen) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.SearchScreen) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookId ->
                    navController.navigate("${Routes.BookDetailsScreen}/$bookId")
                }
            )
        }

        composable("${Routes.BookDetailsScreen}/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailsScreen(
                bookId = bookId,
                onBackClick = { navController.popBackStack() },
                onBorrowClick = { /* Handle borrow */ },
                onWishlistClick = { /* Handle wishlist */ }
            )
        }
    }
}