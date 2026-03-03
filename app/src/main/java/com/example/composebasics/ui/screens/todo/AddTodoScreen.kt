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
import androidx.compose.ui.unit.dp
import com.example.composebasics.ui.components.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    addTodoViewModel: AddTodoViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val taskName = addTodoViewModel.taskName
    val description = addTodoViewModel.description
    val category = addTodoViewModel.category
    val priority = addTodoViewModel.priority

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Task") },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (addTodoViewModel.isValid()) {
                                onSave()
                            }
                        },
                        enabled = addTodoViewModel.isValid()
                    ) {
                        Text("Save")
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
                onValueChange = addTodoViewModel::updateTaskName,
                label = { Text("Task Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = addTodoViewModel::updateDescription,
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            // Category selector
            DropdownSelector(
                options = listOf("Personal", "Work"),
                selected = category,
                onSelected = addTodoViewModel::updateCategory,
                label = "Category"
            )
            if (category == "Select Category") {
                Text(
                    text = "Category is required",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Priority selector
            DropdownSelector(
                options = listOf("Low", "Medium", "High"),
                selected = priority,
                onSelected = addTodoViewModel::updatePriority,
                label = "Priority"
            )
        }
    }
}