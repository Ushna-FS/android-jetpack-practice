package com.example.composebasics.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LIGHT THEME pastel colors
private val LightPastels = listOf(
    Color(0xFFFFF9C4), // yellow
    Color(0xFFFFCDD2), // pink
    Color(0xFFBBDEFB), // blue
    Color(0xFFC8E6C9), // green
    Color(0xFFE1BEE7)  // purple
)


// DARK THEME pastel colors
private val DarkPastels = listOf(
    Color(0xFFFFEB3B), // stronger yellow
    Color(0xFFE91E63), // stronger pink
    Color(0xFF3F51B5), // stronger blue
    Color(0xFF009688), // stronger green
    Color(0xFFCE93D8)  // stronger purple
)

object TodoColors {
    val pending = Color(0xF5F6E765)
    val completed = Color(0xDF6CE070)
}
@Composable
fun getTodoCardColor(index: Int): Color {

    val isDark = isSystemInDarkTheme()

    val baseColor =
        if (isDark)
            DarkPastels[index % DarkPastels.size]
        else
            LightPastels[index % LightPastels.size]
//    return baseColor
    return if (isDark)
        baseColor.copy(alpha = 0.40f)
    else
        baseColor

}