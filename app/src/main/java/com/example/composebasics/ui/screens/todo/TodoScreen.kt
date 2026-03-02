package com.example.composebasics.ui.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import androidx.navigation.NavController
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.components.TodoItem

@Composable
fun TodoScreen(
    navController: NavController,
    viewModel: TodoViewModel
) {

    // Collecting StateFlow as Compose State(recomposition trigger)
    val todos by viewModel.todos.collectAsState()


    // local statte that survives recomposition
    var showCompleted by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf("All") }

    val filteredTodos = todos
        .filter { if (showCompleted) true else !it.isCompleted }
        .filter {
            when (selectedCategory) {
                "Work" -> it.category == "Work"
                "Personal" -> it.category == "Personal"
                else -> true
            }
        }

    Scaffold(
        topBar = {
            TodoTopBar(
                showCompleted = showCompleted,
                onSearchClick = {},
                onToggleCompleted = { showCompleted = !showCompleted })
        },
        floatingActionButton = {
            if (todos.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { navController.navigate(TodoRoutes.ADD_TODO) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn only composes items visible on screen + buffer
            Text(
                stringResource(R.string.todo_text_count, filteredTodos.size),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                FilterChip(
                    selected = selectedCategory == "All",
                    onClick = { selectedCategory = "All" },
                    label = { Text("All") }
                )

                FilterChip(
                    selected = selectedCategory == "Work",
                    onClick = { selectedCategory = "Work" },
                    label = { Text("Work") }
                )

                FilterChip(
                    selected = selectedCategory == "Personal",
                    onClick = { selectedCategory = "Personal" },
                    label = { Text("Personal") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            //  conditional UI (recomposition demo)
            if (todos.isEmpty()) {
                // Default empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = "No tasks, add now",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Button(
                            onClick = { navController.navigate(TodoRoutes.ADD_TODO) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Task")
                        }
                    }
                }

            } else if (filteredTodos.isEmpty()) {

                val message = when (selectedCategory) {
                    "Work" -> "No work related tasks"
                    "Personal" -> "No personal tasks"
                    else -> "No tasks available"
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items(
                        filteredTodos,
                        key = { it.id }
                    ) { todo ->

                        SwipeToDeleteContainer(
                            item = todo,
                            onDelete = { viewModel.deleteTodo(it.id) }
                        ) { item ->

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
}

@Composable
fun TodoTopBar(
    showCompleted: Boolean,
    onSearchClick: () -> Unit,
    onToggleCompleted: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .height(64.dp) //toolbar height
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(R.string.todo_list),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            TextButton(
                onClick = onToggleCompleted,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = if (showCompleted) "Hide Completed" else "Show Completed",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun TodoScreenPreview() {
//    TodoScreen()
//}