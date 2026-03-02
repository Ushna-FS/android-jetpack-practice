package com.example.composebasics.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.composebasics.ui.screens.home.HomeScreen
import com.example.composebasics.ui.screens.todo.AddTodoScreen
import com.example.composebasics.ui.screens.todo.AddTodoViewModel
import com.example.composebasics.ui.screens.todo.TodoRoutes
import com.example.composebasics.ui.screens.todo.TodoScreen
import com.example.composebasics.ui.screens.todo.TodoViewModel
import androidx.compose.runtime.collectAsState

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
                HomeScreen(todoViewModel, navController)
            }

            composable(BottomNavItem.Todo.route) {
                TodoScreen(
                    navController = navController,
                    viewModel = todoViewModel
                )
            }
            composable(TodoRoutes.ADD_TODO) {

                val addTodoViewModel: AddTodoViewModel = viewModel()

                AddTodoScreen(
                    addTodoViewModel = addTodoViewModel,

                    onSave = {
                        if (addTodoViewModel.isValid()) {

                            todoViewModel.addTodo(
                                title = addTodoViewModel.taskName,
                                category = addTodoViewModel.category,
                                description = addTodoViewModel.description.takeIf { it.isNotBlank() },
                                priority = addTodoViewModel.priority.takeIf { it.isNotBlank() }
                            )

                            navController.popBackStack()
                        }
                    },

                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${TodoRoutes.ADD_TODO}/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId")
                val addTodoViewModel: AddTodoViewModel = viewModel()

                // Find the todo from the ViewModel
                val editTodo = todoViewModel.todos.collectAsState().value.find { it.id == todoId }

                // Initialize AddTodoViewModel with existing todo values if editing
                LaunchedEffect(editTodo) {
                    editTodo?.let { addTodoViewModel.setEditingTodo(it) }
                }

                AddTodoScreen(
                    addTodoViewModel = addTodoViewModel,
                    onSave = {
                        if (addTodoViewModel.isValid()) {
                            if (editTodo != null) {
                                // Update existing todo
                                todoViewModel.updateTodo(
                                    id = editTodo.id,
                                    title = addTodoViewModel.taskName,
                                    category = addTodoViewModel.category,
                                    description = addTodoViewModel.description.takeIf { it.isNotBlank() },
                                    priority = addTodoViewModel.priority.takeIf { it.isNotBlank() }
                                )
                            } else {
                                // Add new todo
                                todoViewModel.addTodo(
                                    title = addTodoViewModel.taskName,
                                    category = addTodoViewModel.category,
                                    description = addTodoViewModel.description.takeIf { it.isNotBlank() },
                                    priority = addTodoViewModel.priority.takeIf { it.isNotBlank() }
                                )
                            }
                            navController.popBackStack()
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}