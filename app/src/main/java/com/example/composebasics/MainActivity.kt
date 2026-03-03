package com.example.composebasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.composebasics.data.AppDatabase
import com.example.composebasics.data.TodoRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "todo_db"
        ).build()

        val repository = TodoRepository(db.todoDao())

        setContent {
            MyApp(repository)
        }
    }
}