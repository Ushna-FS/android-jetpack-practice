package com.example.composebasics.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composebasics.ui.screens.home.HomeScreen
import com.example.composebasics.ui.screens.login.LoginScreen
import com.example.composebasics.ui.screens.todo.TodoScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home")
                }
            )
        }
        composable("home") {
            HomeScreen(
                onContinueClick = {navController.navigate("todo")}
            )
        }
        composable("todo") {
            TodoScreen()
        }
    }
}