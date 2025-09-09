package com.assistant.libraries.presentation.views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable(Routes.HomeScreen) {
             HomeScreen()
        }

//        composable(Routes.LoginScreen) {
//            LoginScreen(onBackClick = { navController.popBackStack() })
//        }
//
//        composable(Routes.SignupScreen) {
//            SignUpScreen(onBackClick = { navController.popBackStack() })
//        }
//
//        composable(Routes.MainScreen) {
//            LibraryLighthouseScreen()
//        }
    }
}