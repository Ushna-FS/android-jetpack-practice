package com.example.composebasics.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val title: String,
    val category: String,
    val description: String? = null,
    val priority: String? = null,
    val isCompleted: Boolean = false
)