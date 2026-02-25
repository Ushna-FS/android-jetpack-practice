package com.example.composebasics.ui.screens.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.data.Todo
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import com.example.composebasics.ui.theme.getTodoCardColor
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.theme.TodoColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel
) {

    // Collecting StateFlow as Compose State(recomposition trigger)
    val todos by viewModel.todos.collectAsState()
    val newTodoText by viewModel.newTodoText.collectAsState()


    // local statte that survives recomposition
    var showCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.todo_list)) }, actions = {
                // Filter toggle button
                TextButton(onClick = { showCompleted = !showCompleted }) {
                    Text(if (showCompleted) "Hide Completed" else "Show All")
                }
            })
        }) { paddingValues ->
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
                    onClick = { viewModel.addTodo() }, enabled = newTodoText.isNotBlank()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LazyColumn only composes items visible on screen + buffer
            Text(
                stringResource(R.string.todo_text_count, todos.size),
                style = MaterialTheme.typography.titleMedium
            )

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
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_todos_text))
            }
        }

    }
}


@Composable
fun TodoItem(
    todo: Todo, onToggle: () -> Unit
) {
    // This state is forr specific todo item
    var isExpanded by remember { mutableStateOf(false) }
    val backgroundColor = getTodoCardColor(todo.id)

    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
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
                        checked = todo.isCompleted, onCheckedChange = { onToggle() })

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
                AssistChip(
                    onClick = { },

                    label = {
                        Text(
                            text = if (todo.isCompleted) "Completed" else "Pending",
                            color = Color.Black
                        )
                    },

                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),

                    border = null,

                    colors = AssistChipDefaults.assistChipColors(
                        containerColor =
                            if (todo.isCompleted)
                                TodoColors.completed
                            else
                                TodoColors.pending
                    ),
                    elevation = AssistChipDefaults.assistChipElevation(
                        elevation = 6.dp  // shadow
                    )
                )
            }

            //  appears/disappears based on isExpanded state
            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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
//
//@Preview(showBackground = true)
//@Composable
//fun TodoScreenPreview() {
//    TodoScreen()
//}