package com.example.composebasics.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Todo

@Serializable
object AddTodo

@Serializable
data class EditTodo(
    val todoId: Int
)