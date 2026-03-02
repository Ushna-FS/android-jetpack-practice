package com.example.composebasics.ui.screens.todo

import androidx.lifecycle.ViewModel
import com.example.composebasics.data.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    fun addTodo(
        title: String,
        category: String,
        description: String?,
        priority: String?
    ) {
        if (title.isNotBlank() && category.isNotBlank()) {

            val newTodo = Todo(
                id = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1,
                title = title,
                category = category,
                description = description,
                priority = priority
            )

            _todos.update { currentList -> currentList + newTodo }
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
    fun updateTodo(id: Int, title: String, category: String, description: String?, priority: String?) {
        _todos.update { todos ->
            todos.map { todo ->
                if (todo.id == id) {
                    todo.copy(title = title, category = category, description = description, priority = priority)
                } else todo
            }
        }
    }
}