# Momento de Pausa - Aplicativo de Meditação e Respiração

Este é um aplicativo Android moderno desenvolvido com **Jetpack Compose**, focado em proporcionar momentos de relaxamento através de exercícios de respiração guiada e meditação.

## 🚀 Como construir esta aplicação (Passo a Passo)

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

### 4. Tela de Meditação (`MeditationScreen.kt`)
Onde ocorre a lógica principal e as animações.
- **Estado**: Gerencia o cronômetro (`mutableIntStateOf`) e o status da sessão (`mutableStateOf`).
- **Animação de Respiração**: Utiliza `animateFloatAsState` com `infiniteRepeatable` para criar um círculo que expande e contrai suavemente (efeito de pulsar).
- **Interface**: Composta por um `Box` para sobrepor o círculo animado ao fundo e controles na parte inferior.

### 5. Navegação (`MainActivity.kt`)
Gerencia a troca de telas de forma simples.
- Define um estado `currentScreen` para rastrear qual tela deve ser exibida.
- Utiliza um bloco `when` dentro do `Surface` principal para alternar entre `HomeScreen` e `MeditationScreen`.

### 6. Design e Tema
- Cores relaxantes definidas em `ui/theme/Color.kt`.
- Foco em acessibilidade e feedback visual através das animações do Compose.

---

## 🛠️ Tecnologias Utilizadas
- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary)

---
Desenvolvido como um exemplo de animações interativas e interface fluida no Android.
