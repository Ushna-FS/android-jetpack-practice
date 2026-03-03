package com.example.composebasics.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: Any,
    val title: String,
    val icon: ImageVector
) {
    object HomeItem : BottomNavItem(
        route = Home,
        title = "Home",
        icon = Icons.Default.Home
    )

    object TodoItem : BottomNavItem(
        route = Todo,
        title = "Todos",
        icon = Icons.Default.List
    )
}