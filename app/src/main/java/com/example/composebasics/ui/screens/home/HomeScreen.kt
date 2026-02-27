package com.example.composebasics.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composebasics.R
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.components.TodoItem
import com.example.composebasics.ui.screens.todo.TodoRoutes
import com.example.composebasics.ui.screens.todo.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TodoViewModel,
    navController: NavController,
) {

    val todos by viewModel.todos.collectAsState()

    val pendingTodos = todos.filter { !it.isCompleted }

    Scaffold(
        topBar = {
            HomeTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(TodoRoutes.ADD_TODO) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")

            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "You have ${pendingTodos.size} pending tasks",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    pendingTodos,
                    key = { it.id }
                ) { todo ->

                    SwipeToDeleteContainer(
                        item = todo,
                        onDelete = { viewModel.deleteTodo(it.id) }
                    ) { item ->
                        // TodoItem with toggle functionality
                        TodoItem(
                            todo = item,
                            onToggle = { viewModel.toggleTodo(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopBar() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .padding(horizontal = 16.dp)
        ) {

            // Left Icon
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.tasks),
                    contentDescription = "Tasks Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Center Title
            Text(
                text = stringResource(R.string.my_tasks),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}