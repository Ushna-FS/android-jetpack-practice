package com.example.composebasics.data

data class Todo(
    val id: Int,
    val title: String,
    val category: String,
    val description: String? = null,
    val priority: String? = null,
    val isCompleted: Boolean = false
)