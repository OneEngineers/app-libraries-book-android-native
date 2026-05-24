package com.ones.assistant.presentation.views

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ones.assistant.presentation.views.auth.LoginScreen
import com.ones.assistant.presentation.views.auth.RegisterScreen
import com.ones.assistant.presentation.views.books.BookDetailsScreen
import com.ones.assistant.presentation.views.books.BookReaderScreen
import com.ones.assistant.presentation.views.books.LibraryScreen
import android.net.Uri
import com.ones.assistant.presentation.views.feature.SearchScreen
import com.ones.assistant.presentation.views.feature.WishListScreen
import com.ones.assistant.presentation.views.feature.WishListViewModel
import com.ones.assistant.presentation.views.home.HomeScreen
import com.ones.assistant.presentation.views.podcast.PodcastDetailScreen
import com.ones.assistant.presentation.views.podcast.PodcastScreen
import com.ones.assistant.presentation.views.users.ProfileScreen
import com.ones.assistant.presentation.views.users.SettingsScreen

@Composable
fun MyAppNavigation() {

    val navController = rememberNavController()

    val wishListViewModel: WishListViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {

        // HOME

        composable(Routes.HomeScreen) {

            HomeScreen(
                onSignUpClick = {
                    navController.navigate(Routes.SignupScreen)
                },

                onLoginClick = {
                    navController.navigate(Routes.LoginScreen)
                }
            )
        }

        //  LOGIN

        composable(Routes.LoginScreen) {

            LoginScreen(

                onLoginSuccess = {

                    navController.navigate(Routes.MainScreen) {

                        popUpTo(Routes.HomeScreen) {
                            inclusive = true
                        }
                    }
                },

                onNavigateToRegister = {
                    navController.navigate(Routes.SignupScreen)
                }
            )
        }

        // REGISTER

        composable(Routes.SignupScreen) {

            RegisterScreen(

                onRegisterSuccess = {

                    navController.navigate(Routes.MainScreen) {

                        popUpTo(Routes.HomeScreen) {
                            inclusive = true
                        }
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

        //  MAIN SCREEN

        composable(Routes.MainScreen) {

            WearOneHome(

                booksViewModel = hiltViewModel(),

                onProfileClick = {
                    navController.navigate(Routes.ProfileScreen)
                },

                onSearchClick = {
                    navController.navigate(Routes.SearchScreen)
                },

                onBookClick = { bookId ->

                    navController.navigate(
                        "${Routes.BookDetailsScreen}/$bookId"
                    )
                },

                onLibraryClick = {
                    navController.navigate(Routes.LibraryScreen)
                },

                // Podcast card click
                onPodcastClick = { podcastId ->

                    navController.navigate(
                        "${Routes.PodcastDetailScreen}/$podcastId"
                    )
                },

                // Podcast icon click
                onPodcastIconClick = {

                    navController.navigate(Routes.PodcastScreen)
                },

                onMovieClick = {
                    // TODO
                }
            )
        }

        // WISHLIST

        composable(Routes.WishListScreen) {

            WishListScreen(
                navController = navController,
                wishListViewModel = wishListViewModel
            )
        }

        //  PROFILE

        composable(Routes.ProfileScreen) {

            ProfileScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onLogoutClick = {

                    navController.navigate(Routes.HomeScreen) {

                        popUpTo(Routes.HomeScreen) {
                            inclusive = true
                        }
                    }
                },

                onSettingsClick = {
                    navController.navigate(Routes.SettingsScreen)
                }
            )
        }

        // SETTINGS

        composable(Routes.SettingsScreen) {

            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // SEARCH

        composable(Routes.SearchScreen) {

            SearchScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onBookClick = { bookId ->

                    navController.navigate(
                        "${Routes.BookDetailsScreen}/$bookId"
                    )
                }
            )
        }

        // BOOK DETAILS

        composable(
            "${Routes.BookDetailsScreen}/{bookId}"
        ) { backStackEntry ->

            val bookId =
                backStackEntry.arguments?.getString("bookId") ?: ""

            BookDetailsScreen(

                bookId = bookId,

                onBackClick = {
                    navController.popBackStack()
                },

                onReadClick = { title, pdfUrl ->
                    navController.navigate(Routes.bookReader(title, pdfUrl))
                },

                onWishlistClick = {
                    navController.navigate(Routes.WishListScreen)
                }
            )
        }

        composable(
            "${Routes.BookReaderScreen}/{bookTitle}/{pdfUrl}"
        ) { backStackEntry ->
            val bookTitle = Uri.decode(backStackEntry.arguments?.getString("bookTitle") ?: "")
            val pdfUrl = Uri.decode(backStackEntry.arguments?.getString("pdfUrl") ?: "")

            BookReaderScreen(
                bookTitle = bookTitle,
                pdfUrl = pdfUrl,
                onBackClick = { navController.popBackStack() }
            )
        }

        //LIBRARY

        composable(Routes.LibraryScreen) {

            LibraryScreen(navController)
        }

        // PODCAST LIST

        composable(Routes.PodcastScreen) {

            PodcastScreen(navController)
        }

        //  PODCAST DETAILS

        composable(
            "${Routes.PodcastDetailScreen}/{podcastId}"
        ) { backStackEntry ->

            val podcastId =
                backStackEntry.arguments?.getString("podcastId") ?: ""

            PodcastDetailScreen(

                podcastId = podcastId,

                onBackClick = {
                    navController.popBackStack()
                },

                wishListViewModel = wishListViewModel
            )
        }
    }
}