package com.example.composebasics.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.composebasics.ui.screens.home.HomeScreen
import com.example.composebasics.ui.screens.todo.TodoScreen
import com.example.composebasics.ui.screens.todo.TodoViewModel

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val todoViewModel: TodoViewModel = viewModel()


    val items = listOf(
        BottomNavItem.Home, BottomNavItem.Todo
    )

    Scaffold(
        bottomBar = {

            NavigationBar {

                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                items.forEach { item ->

                    NavigationBarItem(
                        selected = currentRoute == item.route,

                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("home")
                                launchSingleTop = true
                            }
                        },

                        icon = {
                            Icon(item.icon, item.title)
                        },

                        label = {
                            Text(item.title)
                        })
                }
            }
        }) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(BottomNavItem.Home.route) {
                HomeScreen(todoViewModel)
            }

            composable(BottomNavItem.Todo.route) {
                TodoScreen(todoViewModel)
            }
        }
    }
}