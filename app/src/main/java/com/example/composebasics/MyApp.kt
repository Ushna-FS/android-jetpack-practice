package com.example.composebasics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composebasics.navigation.AppNavigation
import com.example.composebasics.ui.theme.ComposeBasicsTheme

@Composable
fun MyApp() {
    ComposeBasicsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun MyAppPreview() {
//    MyApp()
//}