package com.example.composebasics.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.ui.components.SwipeToDeleteContainer
import com.example.composebasics.ui.screens.todo.TodoItem
import com.example.composebasics.ui.screens.todo.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TodoViewModel
) {

    val todos by viewModel.todos.collectAsState()

    val pendingTodos = todos.filter { !it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.home))
                }
            )
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

