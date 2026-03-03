package com.example.composebasics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composebasics.data.TodoRepository
import com.example.composebasics.navigation.AppNavigation
import com.example.composebasics.ui.theme.ComposeBasicsTheme

@Composable
fun MyApp(repository: TodoRepository) {
    ComposeBasicsTheme() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation(repository)
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun MyAppPreview() {
//    MyApp()
//}