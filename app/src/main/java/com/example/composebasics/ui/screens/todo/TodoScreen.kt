package com.example.composebasics.ui.screens.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composebasics.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = viewModel()
) {

    // Collecting StateFlow as Compose State(recomposition trigger)
    val todos by viewModel.todos.collectAsState()
    val newTodoText by viewModel.newTodoText.collectAsState()


    // local statte that survives recomposition
    var showCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                actions = {
                    // Filter toggle button
                    TextButton(onClick = { showCompleted = !showCompleted }) {
                        Text(if (showCompleted) "Hide Completed" else "Show All")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Row for adding new todos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { viewModel.updateNewTodoText(it) },
                    label = { Text("Add new todo") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = { viewModel.addTodo() },
                    enabled = newTodoText.isNotBlank()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn only composes items visible on screen + buffer
            Text("Tasks (${todos.size})", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Filter todos based on showCompleted state
                val filteredTodos = if (showCompleted) {
                    todos
                } else {
                    todos.filter { !it.isCompleted }
                }


                items(
                    items = filteredTodos,
                    key = { todo -> todo.id } // unique key for each item (helps recomposition)
                ) { todo ->
                    // Each item is a composable that recomposes independently
                    TodoItem(
                        todo = todo,
                        onToggle = { viewModel.toggleTodo(todo.id) },
                        onDelete = { viewModel.deleteTodo(todo.id) }
                    )
                }
            }

            //  conditional UI (recomposition demo)
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No todos yet. Add one now!")
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    // This state is forr specific todo item
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Main row with todo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isExpanded = !isExpanded }, // Click to expand
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.isCompleted,
                        onCheckedChange = { onToggle() }
                    )

                    Text(
                        text = todo.title,
                        modifier = Modifier.padding(start = 8.dp),
                        style = if (todo.isCompleted) {
                            MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = TextDecoration.LineThrough
                            )
                        } else {
                            MaterialTheme.typography.bodyLarge
                        }
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            //  appears/disappears based on isExpanded state
            if (isExpanded) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Details: This is additional info about '${todo.title}'",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 36.dp, bottom = 8.dp)
                )

                Text(
                    text = "Status: ${if (todo.isCompleted) "✓ Completed" else "○ Pending"}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    TodoScreen()
}