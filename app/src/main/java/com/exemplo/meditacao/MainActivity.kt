package com.exemplo.meditacao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color(0xFF1E1B29),
                    primary = Color(0xFF8A70D6),
                    secondary = Color(0xFF03DAC6)
                )
            ) {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("Home") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 2. TRANSIÇÃO SUAVE DE ENTRADA E SAÍDA DA HOME
        AnimatedVisibility(
            visible = currentScreen == "Home",
            enter = fadeIn(tween(400)) + slideInHorizontally(tween(400), initialOffsetX = { -it }),
            exit = fadeOut(tween(400)) + slideOutHorizontally(tween(400), targetOffsetX = { -it })
        ) {
            HomeScreen(onNavigate = { currentScreen = "Meditation" })
        }

        // 2. TRANSIÇÃO SUAVE DE ENTRADA E SAÍDA DA TELA DE MEDITAÇÃO
        AnimatedVisibility(
            visible = currentScreen == "Meditation",
            enter = fadeIn(tween(400)) + slideInHorizontally(tween(400), initialOffsetX = { it }),
            exit = fadeOut(tween(400)) + slideOutHorizontally(tween(400), targetOffsetX = { it })
        ) {
            MeditationScreen(onBack = { currentScreen = "Home" })
        }
    }
}