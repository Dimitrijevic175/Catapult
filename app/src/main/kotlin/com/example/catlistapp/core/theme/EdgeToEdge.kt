package com.example.catlistapp.core.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
fun EnableEdgeToEdge(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val context = LocalContext.current
    if (context is ComponentActivity) {
        context.enableEdgeToEdge(
            statusBarColor = statusBarColor,
            navigationBarColor = navigationBarColor,
            isDarkTheme = isDarkTheme,
        )
    }
}

private fun ComponentActivity.enableEdgeToEdge(
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    isDarkTheme: Boolean,
) {
    this@enableEdgeToEdge.enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = statusBarColor.toArgb(),
            darkScrim = statusBarColor.toArgb(),
            detectDarkMode = { isDarkTheme },
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = navigationBarColor.toArgb(),
            darkScrim = navigationBarColor.toArgb(),
            detectDarkMode = { isDarkTheme },
        ),
    )
}