package com.example.composebasics.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composebasics.data.TodoRepository
import com.example.composebasics.ui.screens.login.LoginScreen
import com.example.composebasics.ui.screens.todo.TodoViewModel

@Composable
fun AppNavigation(repository: TodoRepository) {
    val navController = rememberNavController()
    val todoViewModel: TodoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(repository) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen(todoViewModel)
        }
    }
}