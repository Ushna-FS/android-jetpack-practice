package com.example.composebasics.ui.screens.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddTodoViewModel : ViewModel() {

    var taskName by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var category by mutableStateOf("Select Category") // default
        private set

    var priority by mutableStateOf("Select Priority") // default
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
        return taskName.isNotBlank() && category != "Select Category"
    }
}