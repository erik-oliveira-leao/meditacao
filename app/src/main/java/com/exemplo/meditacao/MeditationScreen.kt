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