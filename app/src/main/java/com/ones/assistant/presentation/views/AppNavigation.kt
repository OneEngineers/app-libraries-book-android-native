package com.ones.assistant.presentation.views

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ones.assistant.presentation.views.auth.LoginScreen
import com.ones.assistant.presentation.views.auth.RegisterScreen
import com.ones.assistant.presentation.views.books.BookDetails
import com.ones.assistant.presentation.views.books.BookDetailsScreen
import com.ones.assistant.presentation.views.books.BookReaderScreen
import com.ones.assistant.presentation.views.books.LibraryScreen
import android.net.Uri
import com.ones.assistant.presentation.viewmodel.ReadingHistoryViewModel
import com.ones.assistant.presentation.views.feature.ReadingHistoryScreen
import com.ones.assistant.presentation.views.feature.SearchScreen
import com.ones.assistant.presentation.views.feature.WishListScreen
import com.ones.assistant.presentation.views.feature.WishListViewModel
import com.ones.assistant.presentation.views.feature.WishlistItem
import com.ones.assistant.presentation.views.home.HomeScreen
import com.ones.assistant.presentation.views.podcast.PodcastDetailScreen
import com.ones.assistant.presentation.views.podcast.PodcastScreen
import com.ones.assistant.presentation.views.users.ProfileScreen
import com.ones.assistant.presentation.views.users.SettingsScreen

@Composable
fun MyAppNavigation() {

    val navController = rememberNavController()

    val wishListViewModel: WishListViewModel = viewModel()
    val readingHistoryViewModel: ReadingHistoryViewModel = viewModel()

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
                wishListViewModel = wishListViewModel,

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
                wishListViewModel = wishListViewModel,
                onBookClick = { bookId ->
                    navController.navigate(
                        "${Routes.BookDetailsScreen}/$bookId"
                    )
                },
                onPodcastClick = { podcastId ->
                    navController.navigate(
                        "${Routes.PodcastDetailScreen}/$podcastId"
                    )
                }
            )
        }

        //  PROFILE

        composable(Routes.ProfileScreen) {

            ProfileScreen(

                onBackClick = {
                    navController.popBackStack()
                },

                onLogoutClick = {
                    com.ones.assistant.utilities.UserStateManager.clearUser()
                    navController.navigate(Routes.HomeScreen) {

                        popUpTo(Routes.HomeScreen) {
                            inclusive = true
                        }
                    }
                },

                onSettingsClick = {
                    navController.navigate(Routes.SettingsScreen)
                },

                onHistoryClick = {
                    navController.navigate(Routes.ReadingHistoryScreen)
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
                },

                onPodcastClick = { podcastId ->
                    navController.navigate(
                        "${Routes.PodcastDetailScreen}/$podcastId"
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

                onReadClick = { book ->
                    readingHistoryViewModel.addToHistory(book)
                    navController.navigate(Routes.bookReader(book.title, book.pdfUrl))
                },

                onWishlistClick = { book ->
                    wishListViewModel.addFavorite(
                        WishlistItem.Book(
                            id = book.id,
                            title = book.title,
                            author = book.author,
                            coverUrl = book.coverUrl
                        )
                    )
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
//        composable(Routes.AllBooksScreen) {
//
//            AllBooksScreen(
//                navController = navController
//            )
//        }

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
                }
            )
        }

        // READING HISTORY

        composable(Routes.ReadingHistoryScreen) {
            ReadingHistoryScreen(
                viewModel = readingHistoryViewModel,
                onBackClick = { navController.popBackStack() },
                onBookClick = { bookId ->
                    navController.navigate("${Routes.BookDetailsScreen}/$bookId")
                }
            )
        }
    }
}