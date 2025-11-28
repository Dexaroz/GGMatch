import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import com.pamn.ggmatch.R
import kotlinx.coroutines.launch

private const val SWIPE_THRESHOLD_DP = 150

data class ProfileCard(val name: String, val age: Int, val description: String)

@Composable
fun SwipeDemo(modifier: Modifier = Modifier) {
    val cards = remember {
        listOf(
            ProfileCard("Laura", 28, "Apasionada por el diseño y el arte moderno. Su pasión es el código Kotlin y Compose."),
            ProfileCard("Carlos", 32, "Ingeniero de software y amante del senderismo. Siempre buscando la mejor solución arquitectónica."),
            ProfileCard("María", 25, "Estudiante de música y adicta al café. Le gusta la composición algorítmica y los patrones de diseño.")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var swipeDirection by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val animatedColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 0),
        label = "Background Color Animation"
    )

    val swipeThresholdPx = with(LocalDensity.current) { SWIPE_THRESHOLD_DP.dp.toPx() }

    fun nextCard(direction: Int) {
        val nextIndex = (currentIndex + 1) % cards.size
        currentIndex = if (nextIndex < 0) cards.size - 1 else nextIndex
        offsetX = 0f
    }

    suspend fun handleAction(direction: Int) {
        val color = if (direction > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
        backgroundColor = color
        swipeDirection = direction

        nextCard(direction)

        backgroundColor = Color.White
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(animatedColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            val currentCard = cards[currentIndex]
            val nextCardIndex = (currentIndex + 1) % cards.size
            val nextCard = cards[nextCardIndex]

            // Factor de animación para la siguiente tarjeta
            val progress = (offsetX / swipeThresholdPx).coerceIn(-1f, 1f)
            val nextCardScale = 0.95f + 0.05f * progress.absoluteValue
            val nextCardAlpha = 0.5f + 0.5f * progress.absoluteValue

            // Tarjeta siguiente (debajo)
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(nextCardScale)
                    .alpha(nextCardAlpha),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = nextCard.name + ", " + nextCard.age,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 64.dp)
                    )
                    Text(
                        text = "Tarjeta #${currentIndex + 2} de ${cards.size}",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Text(
                        text = nextCard.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Foto de Perfil",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .size(240.dp)
                    )
                }
            }

            // Tarjeta actual (arriba)
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x

                                backgroundColor = when {
                                    offsetX > 0 -> Color(0xFFE8F5E9)
                                    offsetX < 0 -> Color(0xFFFFEBEE)
                                    else -> Color.White
                                }
                            },
                            onDragEnd = {
                                val success = when {
                                    offsetX > swipeThresholdPx -> {
                                        coroutineScope.launch { handleAction(1) }
                                        true
                                    }
                                    offsetX < -swipeThresholdPx -> {
                                        coroutineScope.launch { handleAction(-1) }
                                        true
                                    }
                                    else -> {
                                        backgroundColor = Color.White
                                        offsetX = 0f
                                        false
                                    }
                                }

                                if (!success) {
                                    backgroundColor = Color.White
                                    offsetX = 0f
                                }
                            }
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentCard.name + ", " + currentCard.age,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 64.dp)
                    )
                    Text(
                        text = "Tarjeta #${currentIndex + 1} de ${cards.size}",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Text(
                        text = currentCard.description,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Foto de Perfil",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .size(240.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { coroutineScope.launch { handleAction(-1) } },
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFFEBEE), MaterialTheme.shapes.extraLarge)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.no),
                    contentDescription = "Nope",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { coroutineScope.launch { handleAction(1) } },
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFE8F5E9), MaterialTheme.shapes.extraLarge)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.yes),
                    contentDescription = "Like",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
