package com.example.composebasics.ui.screens.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.composebasics.data.Todo

class AddTodoViewModel : ViewModel() {

    private val defaultCategory = ""
    private val defaultPriority = ""
    var taskName by mutableStateOf("")
    private set

    var description by mutableStateOf("")
    private set

    var category by mutableStateOf(defaultCategory) // default
    private set

    var priority by mutableStateOf(defaultPriority) // default
    private set
    var editingTodo: Todo? = null
    private set

    fun updateTaskName(name: String) {
        taskName = name
    }

    fun updateDescription(desc: String) {
        description = desc
    }

    fun updateCategory(cat: String) {
        category = cat
    }

    fun updatePriority(pri: String) {
        priority = pri
    }

    fun isValid(): Boolean {
        return taskName.isNotBlank() && category != defaultCategory
    }

    fun setEditingTodo(todo: Todo) {
        editingTodo = todo
        // initialize fields with existing todo values
        taskName = todo.title
        description = todo.description ?: ""
        category = todo.category
        priority = todo.priority ?: defaultPriority
    }
}