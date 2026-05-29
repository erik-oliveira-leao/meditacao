# Momento de Pausa - Aplicativo de Meditação e Respiração

Este é um aplicativo Android moderno desenvolvido com **Jetpack Compose**, focado em proporcionar momentos de relaxamento através de exercícios de respiração guiada e meditação.

## Como construir esta aplicação (Passo a Passo)

### 1. Configuração do Projeto
No Android Studio, crie um novo projeto usando o template **"Empty Compose Activity"**.
*   **Nome:** Animações Interativas
*   **Linguagem:** Kotlin
*   **Build Configuration:** Kotlin DSL (libs.versions.toml)

### 2. Configuração das Dependências
Para utilizar a coleção completa de ícones (como o de Spa e Volume), é necessário adicionar a biblioteca estendida.

*   **Arquivo `gradle/libs.versions.toml`**: Adicione no bloco `[libraries]`:
    ```toml
    androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
    ```
*   **Arquivo `app/build.gradle.kts`**: Adicione no bloco `dependencies`:
    ```kotlin
    implementation(libs.androidx.compose.material.icons.extended)
    ```
*   **Sincronize o Gradle** clicando em "Sync Now".

### 3. Tela Inicial (`HomeScreen.kt`)
Ponto de entrada do app que convida o usuário a iniciar uma sessão.
- Utiliza um `Column` centralizado.
- Exibe o ícone `Icons.Default.Spa`.
- Possui um `Button` que aciona a navegação para a tela de meditação.
```kotlin
package com.exemplo.meditacao

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = null,
            tint = Color(0xFFB39DDB),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Momento de Pausa",
            fontSize = 24.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onNavigate,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Selecionar Sessão", color = Color.White)
        }
    }
}
```

### 4. Tela de Meditação (`MeditationScreen.kt`)
Onde ocorre a lógica principal e as animações.
- **Estado**: Gerencia o cronômetro (`mutableIntStateOf`) e o status da sessão (`mutableStateOf`).
- **Animação de Respiração**: Utiliza `animateFloatAsState` com `infiniteRepeatable` para criar um círculo que expande e contrai suavemente (efeito de pulsar).
- **Interface**: Composta por um `Box` para sobrepor o círculo animado ao fundo e controles na parte inferior.
```kotlin
package com.exemplo.meditacao

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(onBack: () -> Unit) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 3. ANIMAÇÃO DO BOTÃO "INICIAR MEDITAÇÃO" (SCALE)
    val buttonScale = remember { Animatable(1f) }

    // 4. ANIMAÇÃO DE FADE IN DO TEXTO (1 SEGUNDO DE DURAÇÃO)
    val textAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    // 5. ANIMAÇÃO DE BOUNCE DO MENU E DOS ÍCONES (EFEITO ELÁSTICO)
    val menuScale by animateFloatAsState(
        targetValue = if (isMenuOpen) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "MenuBounce"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sessão Relaxante") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                        Icon(
                            imageVector = if (isMenuOpen) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 4. TEXTO INTRODUTÓRIO COM FADE IN
                Text(
                    text = "Feche os olhos, respire fundo e esvazie a sua mente. O seu momento de paz começa agora.",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.alpha(textAlpha.value)
                )

                Spacer(modifier = Modifier.height(60.dp))

                // 3. BOTÃO COM INTERAÇÃO DE TOQUE E EFEITO SCALE
                Box(
                    modifier = Modifier
                        .scale(buttonScale.value)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { /* Ação de clique definitivo */ },
                                onPress = {
                                    scope.launch { buttonScale.animateTo(1.1f, tween(150)) }
                                    tryAwaitRelease()
                                    scope.launch { buttonScale.animateTo(1f, tween(150)) }
                                }
                            )
                        }
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(30.dp))
                        .padding(horizontal = 40.dp, vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Iniciar Meditação",
                        fontSize = 18.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // 5. CONFIGURAÇÃO DOS ÍCONES DE MENU COM EFEITO BOUNCE INDIVIDUAL
            if (menuScale > 0f) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2942)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                        .scale(menuScale)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        IconButton(onClick = {}, modifier = Modifier.scale(menuScale)) {
                            Icon(Icons.Default.VolumeUp, "Som", tint = Color.White)
                        }
                        IconButton(onClick = {}, modifier = Modifier.scale(menuScale)) {
                            Icon(Icons.Default.Timer, "Timer", tint = Color.White)
                        }
                        IconButton(onClick = {}, modifier = Modifier.scale(menuScale)) {
                            Icon(Icons.Default.Settings, "Configurações", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}
```

### 5. Navegação (`MainActivity.kt`)
Gerencia a troca de telas de forma simples.
- Define um estado `currentScreen` para rastrear qual tela deve ser exibida.
- Utiliza um bloco `when` dentro do `Surface` principal para alternar entre `HomeScreen` e `MeditationScreen`.
```kotlin
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
```

### 6. Design e Tema
- Cores relaxantes definidas em `ui.theme/Color.kt`.
- Foco em acessibilidade e feedback visual através das animações do Compose.<br>
`Color.kt`
```kotlin
package com.exemplo.meditacao.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```
 `Theme.kt`
 ```kotlin
 package com.exemplo.meditacao.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AnimaçõesinterativasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
 ```
  `Type.kt`
  ```kotlin
  package com.exemplo.meditacao.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
  ```

---

## 🛠️ Tecnologias Utilizadas
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary)

---

