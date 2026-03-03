package com.example.composebasics.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composebasics.R
import com.example.composebasics.data.Todo
import com.example.composebasics.navigation.AddTodo
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.components.TodoItem
import com.example.composebasics.ui.screens.todo.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TodoViewModel,
    navController: NavController,
) {

    val todos by viewModel.todos.collectAsState()

    val priorityTodos = todos.filter {
        !it.isCompleted && it.priority?.lowercase() in listOf("high", "medium")
    }
    val recentlyCompleted = todos
        .filter { it.isCompleted }
        .takeLast(3) // show latest 3 completed

    Scaffold(
        topBar = {
            HomeTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddTodo) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")

            }
        }
    ) { padding ->
        if (todos.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks yet.\nTap ' + ' to add one",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // priority task section
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "PRIORITY TASKS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (priorityTodos.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "No high priority tasks yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(priorityTodos, key = { it.id }) { todo ->
                    SwipeToDeleteContainer(
                        item = todo,
                        onDelete = { viewModel.deleteTodo(it.id) }
                    ) { item ->
                        PriorityTodoItem(
                            todo = item,
                            onToggle = { viewModel.toggleTodo(item.id) }
                        )
                    }
                }
            }

            //recently completed section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "RECENTLY COMPLETED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (recentlyCompleted.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "You need to complete a task",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(recentlyCompleted, key = { it.id }) { todo ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.5f) //greyed out
                    ) {
                        TodoItem(
                            todo = todo,
                            onToggle = { }
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


@Composable
fun PriorityTodoItem(
    todo: Todo,
    onToggle: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp) // space for chip overlap
    ) {

        TodoItem(
            todo = todo,
            onToggle = onToggle
        )

        // Priority Chip
        if (todo.priority == "High" || todo.priority == "Medium") {

            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = todo.priority,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = when (todo.priority) {

                        "High" -> MaterialTheme.colorScheme.error
                        "Medium" -> MaterialTheme.colorScheme.tertiary

                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = AssistChipDefaults.assistChipElevation(
                    elevation = 6.dp
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 3.dp, y = (-25).dp) // overlap effect
            )
        }
    }
}