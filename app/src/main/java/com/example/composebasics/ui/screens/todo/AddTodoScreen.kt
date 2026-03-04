package com.example.composebasics.ui.screens.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composebasics.R
import com.example.composebasics.ui.components.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreenContent(
    taskName: String,
    description: String,
    category: String,
    priority: String,
    isValid: Boolean,
    onTaskNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onPrioritySelected: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val defaultCategory = stringResource(R.string.select_category)
    val defaultPriority = stringResource(R.string.select_priority)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_todo_topbar)) },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.ic_cancel_desc)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSave,
                        enabled = isValid
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = onTaskNameChange,
                label = { Text(stringResource(R.string.task_name_field)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(R.string.description_field)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            // Category selector
            DropdownSelector(
                options = listOf(stringResource(R.string.personal), stringResource(R.string.work)),
                selected = category.ifBlank { defaultCategory },
                onSelected = onCategorySelected,
                label = stringResource(R.string.category_label)
            )
            if (category.isBlank()) {
                Text(
                    text = stringResource(R.string.category_req),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Priority selector
            DropdownSelector(
                options = listOf(
                    stringResource(R.string.low),
                    stringResource(R.string.medium), stringResource(R.string.high)
                ),
                selected = priority.ifBlank { defaultPriority },
                onSelected = onPrioritySelected,
                label = stringResource(R.string.priority)
            )
        }
    }
}

@Composable
fun AddTodoScreen(
    addTodoViewModel: AddTodoViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

    AddTodoScreenContent(
        taskName = addTodoViewModel.taskName,
        description = addTodoViewModel.description,
        category = addTodoViewModel.category,
        priority = addTodoViewModel.priority,
        isValid = addTodoViewModel.isValid(),
        onTaskNameChange = addTodoViewModel::updateTaskName,
        onDescriptionChange = addTodoViewModel::updateDescription,
        onCategorySelected = addTodoViewModel::updateCategory,
        onPrioritySelected = addTodoViewModel::updatePriority,
        onSave = {
            if (addTodoViewModel.isValid()) {
                onSave()
            }
        },
        onCancel = onCancel
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddTodo() {

    AddTodoScreenContent(
        taskName = "",
        description = "",
        category = "",
        priority = "",
        isValid = false,
        onTaskNameChange = {},
        onDescriptionChange = {},
        onCategorySelected = {},
        onPrioritySelected = {},
        onSave = {},
        onCancel = {}
    )
}