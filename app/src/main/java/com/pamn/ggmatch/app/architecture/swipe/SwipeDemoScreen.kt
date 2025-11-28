import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import com.pamn.ggmatch.app.architecture.swipe.logic.SwipeState
import com.pamn.ggmatch.app.architecture.swipe.model.getTestCards
import com.pamn.ggmatch.app.architecture.swipe.ui.SwipeButtons
import com.pamn.ggmatch.app.architecture.swipe.ui.SwipeCard

private const val SWIPE_THRESHOLD_DP = 150

@Composable
fun SwipeDemoScreen(modifier: Modifier = Modifier) {
    val cards = getTestCards()
    val coroutineScope = rememberCoroutineScope()
    val swipeThresholdPx = with(LocalDensity.current) { SWIPE_THRESHOLD_DP.dp.toPx() }
    val swipeState = remember { SwipeState(coroutineScope, swipeThresholdPx) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(swipeState.backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            val currentCard = cards[swipeState.currentIndex]
            val nextCard = cards[(swipeState.currentIndex + 1) % cards.size]

            val progress = (swipeState.offsetX / swipeThresholdPx).coerceIn(-1f, 1f)
            val nextCardScale = 0.95f + 0.05f * progress.absoluteValue
            val nextCardAlpha = 0.5f + 0.5f * progress.absoluteValue

            SwipeCard(card = nextCard, scale = nextCardScale, alpha = nextCardAlpha)

            SwipeCard(
                card = currentCard,
                offsetX = swipeState.offsetX,
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            if (!swipeState.isAnimating) {
                                change.consumePositionChange()
                                swipeState.offsetX += dragAmount.x
                                swipeState.backgroundColor = when {
                                    swipeState.offsetX > 0 -> androidx.compose.ui.graphics.Color(0xFFE8F5E9)
                                    swipeState.offsetX < 0 -> androidx.compose.ui.graphics.Color(0xFFFFEBEE)
                                    else -> androidx.compose.ui.graphics.Color.White
                                }
                            }
                        },
                        onDragEnd = {
                            if (swipeState.isAnimating) return@detectDragGestures

                            when {
                                swipeState.offsetX > swipeThresholdPx -> swipeState.swipe(1, cards.size)
                                swipeState.offsetX < -swipeThresholdPx -> swipeState.swipe(-1, cards.size)
                                else -> {
                                    swipeState.offsetX = 0f
                                    swipeState.backgroundColor = androidx.compose.ui.graphics.Color.White
                                }
                            }
                        }
                    )
                }
            )
        }

        SwipeButtons(
            onNope = { swipeState.swipe(-1, cards.size) },
            onLike = { swipeState.swipe(1, cards.size) }
        )
    }
}
