package com.example.composebasics.di

import android.content.Context
import androidx.room.Room
import com.example.composebasics.data.AppDatabase
import com.example.composebasics.data.TodoDao
import com.example.composebasics.data.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao {
        return db.todoDao()
    }

    @Provides
    fun provideRepository(dao: TodoDao): TodoRepository {
        return TodoRepository(dao)
    }
}