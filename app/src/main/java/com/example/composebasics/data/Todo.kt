package com.example.composebasics.data

data class Todo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)