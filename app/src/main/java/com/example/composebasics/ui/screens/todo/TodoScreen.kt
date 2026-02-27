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
                stringResource(R.string.todo_text_count, todos.size),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()
            ) {
                // Filter todos based on showCompleted state
                val filteredTodos = if (showCompleted) {
                    todos
                } else {
                    todos.filter { !it.isCompleted }
                }


                items(
                    filteredTodos,
                    key = { todo -> todo.id }
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
        //  conditional UI (recomposition demo)
        if (todos.isEmpty()) {
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
        modifier = Modifier
            .fillMaxWidth()
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
                    text = if (showCompleted) "Hide Completed" else "Show All",
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