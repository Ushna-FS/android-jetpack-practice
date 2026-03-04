package com.example.composebasics.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.composebasics.ui.screens.home.HomeScreen
import com.example.composebasics.ui.screens.todo.AddTodoScreen
import com.example.composebasics.ui.screens.todo.AddTodoViewModel
import com.example.composebasics.ui.screens.todo.TodoScreen
import com.example.composebasics.ui.screens.todo.TodoViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.toRoute

@Composable
fun MainScreen(
    todoViewModel: TodoViewModel
) {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.HomeItem, BottomNavItem.TodoItem
    )

    Scaffold(
        bottomBar = {

            NavigationBar {

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination


                items.forEach { item ->

                    NavigationBarItem(
                        selected =
                            when (item) {
                                BottomNavItem.HomeItem ->
                                    currentDestination?.route?.contains("Home") == true

                                BottomNavItem.TodoItem ->
                                    currentDestination?.route?.contains("Todo") == true
                            },
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo<Home>()
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
            startDestination = Home,
            modifier = Modifier.padding(padding)
        ) {

            composable<Home> {
                HomeScreen(todoViewModel, navController)
            }

            composable<Todo> {
                TodoScreen(
                    navController = navController,
                    viewModel = todoViewModel
                )
            }
            composable<AddTodo> {

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
            composable<EditTodo> { backStackEntry ->

                val route = backStackEntry.toRoute<EditTodo>()
                val todoId = route.todoId

                val addTodoViewModel: AddTodoViewModel = viewModel()

                val editTodo = todoViewModel.todos.collectAsState().value.find {
                    it.id == todoId
                }

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