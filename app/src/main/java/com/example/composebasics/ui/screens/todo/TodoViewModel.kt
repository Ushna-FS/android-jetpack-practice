package com.example.composebasics.ui.screens.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasics.data.Todo
import com.example.composebasics.data.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    val todos: StateFlow<List<Todo>> = repository.todos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private var recentlyDeletedTodo: Todo? = null

    fun addTodo(
        title: String,
        category: String,
        description: String?,
        priority: String?
    ) {
        if (title.isNotBlank() && category.isNotBlank()) {
            val newTodo = Todo(
                id = 0,
                title = title,
                category = category,
                description = description,
                priority = priority,
                isCompleted = false
            )
            viewModelScope.launch {
                repository.addTodo(newTodo)
            }
        }
    }

    fun updateTodo(
        id: Int,
        title: String,
        category: String,
        description: String?,
        priority: String?
    ) {
        viewModelScope.launch {
            val todo = todos.value.find { it.id == id } ?: return@launch
            repository.updateTodo(
                todo.copy(
                    title = title,
                    category = category,
                    description = description,
                    priority = priority
                )
            )
        }
    }
    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            val todo = todos.value.find { it.id == id } ?: return@launch
            recentlyDeletedTodo = todo
            repository.deleteTodo(todo)
        }
    }

    fun restoreTodo() {
        viewModelScope.launch {
            recentlyDeletedTodo?.let {
                repository.addTodo(it)
                recentlyDeletedTodo = null
            }
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            val todo = todos.value.find { it.id == id } ?: return@launch
            val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
            repository.updateTodo(updatedTodo)
        }
    }
}