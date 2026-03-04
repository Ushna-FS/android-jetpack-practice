package com.example.composebasics.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.composebasics.data.Todo
import com.example.composebasics.ui.theme.TodoColors
import com.example.composebasics.ui.theme.getTodoCardColor

@Composable
fun TodoItem(
    todo: Todo, onToggle: () -> Unit, forceExpanded: Boolean = false
) {
    // This state is forr specific todo item
    var isExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(forceExpanded) {
        if (forceExpanded) isExpanded = true
    }

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
                if (!todo.description.isNullOrBlank()) {
                    Text(
                        text = "Description: ${todo.description}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 36.dp, bottom = 8.dp)
                    )
                }

                Text(
                    text = "Status: ${if (todo.isCompleted) "✓ Completed" else "○ Pending"}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        }
    }
}