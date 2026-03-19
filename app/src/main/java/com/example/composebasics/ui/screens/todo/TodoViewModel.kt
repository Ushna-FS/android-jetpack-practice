package com.example.composebasics.ui.screens.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composebasics.data.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    // State for new todo input
    private val _newTodoText = MutableStateFlow("")
    val newTodoText: StateFlow<String> = _newTodoText.asStateFlow()

    // Initializing with some sample data
    init {
        viewModelScope.launch {
            _todos.value = listOf(
                Todo(1, "Learn Jetpack Compose"),
                Todo(2, "Build a Todo app", true),
                Todo(3, "Master state management")
            )
        }
    }

    fun updateNewTodoText(text: String) {
        _newTodoText.value = text
    }

    fun addTodo() {
        if (_newTodoText.value.isNotBlank()) {
            val newTodo = Todo(
                id = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1,
                title = _newTodoText.value
            )
            // Update state using immutable approach
            _todos.update { currentList -> currentList + newTodo }
            _newTodoText.value = ""
        }
    }

    fun toggleTodo(id: Int) {
        _todos.update { todos ->
            todos.map { todo ->
                if (todo.id == id) {
                    todo.copy(isCompleted = !todo.isCompleted) // Immutable update
                } else {
                    todo
                }
            }
        }
    }

    fun deleteTodo(id: Int) {
        _todos.update { todos ->
            todos.filter { it.id != id }
        }
    }
}