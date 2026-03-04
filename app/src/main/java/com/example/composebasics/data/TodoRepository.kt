package com.example.composebasics.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val dao: TodoDao) {

    val todos: Flow<List<Todo>> = dao.getTodos()

    suspend fun addTodo(todo: Todo) {
        dao.insert(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        dao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        dao.delete(todo)
    }
}