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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composebasics.R
import com.example.composebasics.data.Todo
import com.example.composebasics.navigation.AddTodo
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.components.TodoItem
import com.example.composebasics.ui.screens.todo.TodoViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen( //stateful
    viewModel: TodoViewModel,
    navController: NavController
) {

    val todos by viewModel.todos.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    HomeScreenContent(
        todos = todos,
        onAddClick = { navController.navigate(AddTodo) },
        onDelete = { todo ->

            scope.launch {

                viewModel.deleteTodo(todo.id)

                val result = snackbarHostState.showSnackbar(
                    message = "Task deleted",
                    actionLabel = "UNDO"
                )

                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.restoreTodo()
                }
            }
        },
        onToggle = { viewModel.toggleTodo(it) },
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent( //stateless
    todos: List<Todo>,
    onAddClick: () -> Unit,
    onDelete: (Todo) -> Unit,
    onToggle: (Int) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val priorityTodos = todos.filter {
        !it.isCompleted && it.priority?.lowercase() in listOf("high", "medium")
    }
    val recentlyCompleted = todos
        .filter { it.isCompleted }
        .takeLast(3)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HomeTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.ic_add_desc))

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
                    text = stringResource(R.string.no_tasks),
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
                    text = stringResource(R.string.priority_tasks),
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
                            text = stringResource(R.string.no_priority),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(priorityTodos, key = { it.id }) { todo ->
                    SwipeToDeleteContainer(
                        item = todo,
                        onDelete = { onDelete(todo) }
                    ) { item ->
                        PriorityTodoItem(
                            todo = item,
                            onToggle = { onToggle(item.id) }
                        )
                    }
                }
            }

            //recently completed section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.recently_completed),
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
                            text = stringResource(R.string.complete_txt),
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
                            onToggle = { },
                            forceExpanded = false
                        )
                    }
                }
            }
        }
    }
}

//previewing
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val sampleTodos = listOf(
        Todo(1, "Buy groceries", "Personal", priority = "High"),
        Todo(2, "Finish assignment", "Work", priority = "Medium"),
        Todo(3, "Read book", "Personal", isCompleted = true)
    )

    HomeScreenContent(
        todos = sampleTodos,
        onAddClick = {},
        onDelete = {},
        onToggle = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
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
                    contentDescription = stringResource(R.string.ic_task_desc),
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
            onToggle = onToggle,
            forceExpanded = false
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