package com.example.composebasics.ui.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composebasics.data.Todo
import com.example.composebasics.navigation.AddTodo
import com.example.composebasics.navigation.EditTodo
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

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTodos = todos
        .filter { if (showCompleted) true else !it.isCompleted }
        .filter {
            when (selectedCategory) {
                "Work" -> it.category == "Work"
                "Personal" -> it.category == "Personal"
                else -> true
            }
        }
        .filter { todo ->
            searchQuery.isBlank() ||
                    todo.title.contains(searchQuery, ignoreCase = true) ||
                    (todo.description?.contains(searchQuery, ignoreCase = true) == true)
        }

    Scaffold(
        topBar = {
            TodoTopBar(
                showCompleted = showCompleted,
                onSearchClick = { isSearching = !isSearching },
                onToggleCompleted = { showCompleted = !showCompleted })
        },
        floatingActionButton = {
            if (todos.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddTodo) },
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
            if (isSearching) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

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
                            onClick = { navController.navigate(AddTodo) },
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
                        val extraTopPadding = if (filteredTodos.indexOf(todo) == 0) 18.dp else 0.dp

                        SwipeToDeleteContainer(
                            item = todo,
                            onDelete = { viewModel.deleteTodo(it.id) }
                        ) { item ->
                            val descriptionMatched = searchQuery.isNotBlank() &&
                                    item.description?.contains(
                                        searchQuery,
                                        ignoreCase = true
                                    ) == true

                            EditableTodoItem(
                                todo = item,
                                onToggle = { viewModel.toggleTodo(item.id) },
                                navController = navController,
                                modifier = Modifier.padding(top = extraTopPadding),
                                forceExpanded = descriptionMatched
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

@Composable
fun EditableTodoItem(
    todo: Todo,
    onToggle: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    forceExpanded: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        TodoItem(
            todo = todo,
            onToggle = onToggle,
            forceExpanded = forceExpanded
        )

        SmallFloatingActionButton(
            onClick = {
                navController.navigate(EditTodo(todo.id))
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 2.dp, y = (-18).dp)// overlap
                .size(34.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Todo",
                modifier = Modifier.size(16.dp) // small edit icon
            )
        }
    }
}

//preview
@Preview(showBackground = true)
@Composable
fun EditableTodoItemPreview() {

    // Fake todo for preview
    val sampleTodo = Todo(
        id = 1,
        title = "Buy groceries",
        description = "Milk, Eggs, Bread",
        isCompleted = false,
        category = "Personal"
    )

    // Dummy NavController for preview
    val navController = rememberNavController()

    MaterialTheme {
        EditableTodoItem(
            todo = sampleTodo,
            onToggle = {},
            navController = navController
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TodoScreenPreview() {
//    TodoScreen()
//}