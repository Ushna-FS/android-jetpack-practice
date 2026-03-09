package com.example.composebasics.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 300,
    content: @Composable (T) -> Unit
) {

    var isRemoved by remember(item) { mutableStateOf(false) }
    var deleteTriggered by remember(item) { mutableStateOf(false) }


    val dismissState = remember(item) {
        SwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            positionalThreshold = { it * 0.6f }  // require 60% swipe instead of default ~30%
        )
    }

    LaunchedEffect(dismissState.currentValue) {
        if (
            dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
            !deleteTriggered
        ) {
            deleteTriggered = true
            isRemoved = true
            delay(animationDuration.toLong())
            onDelete(item)
        }

    }

    key(item) {
        AnimatedVisibility(
            visible = !isRemoved,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {
                    DeleteBackground(dismissState)
                },
                content = {
                    content(item)
                }
            )
        }
    }
}

@Composable
fun DeleteBackground(dismissState: SwipeToDismissBoxState) {

    val isSwiping = dismissState.dismissDirection ==
            SwipeToDismissBoxValue.EndToStart

    val color = if (isSwiping)
        Color.Red
    else
        Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {

        if (isSwiping) {   // show icon only while swiping
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}