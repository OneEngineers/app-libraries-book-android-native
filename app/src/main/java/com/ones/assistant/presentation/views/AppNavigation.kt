package com.ones.assistant.presentation.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ones.assistant.presentation.views.auth.LoginScreen
import com.ones.assistant.presentation.views.auth.RegisterScreen
import com.ones.assistant.presentation.views.books.BookDetailsScreen
import com.ones.assistant.presentation.views.feature.SearchScreen
import com.ones.assistant.presentation.views.home.HomeScreen
import com.ones.assistant.presentation.views.users.ProfileScreen
import com.ones.assistant.presentation.views.users.SettingsScreen

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
            WearOneHome(
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