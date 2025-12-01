package com.pamn.ggmatch.app.architecture.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.architecture.swipe.logic.SwipeState
import com.pamn.ggmatch.app.architecture.swipe.model.getTestCards
import com.pamn.ggmatch.app.architecture.swipe.ui.swipeButtons
import com.pamn.ggmatch.app.architecture.swipe.ui.swipeCard
import kotlin.math.absoluteValue

private const val SWIPE_THRESHOLD_DP = 150

@Composable
fun swipeDemoScreen(modifier: Modifier = Modifier) {
    val cards = getTestCards()
    val coroutineScope = rememberCoroutineScope()
    val swipeThresholdPx = with(LocalDensity.current) { SWIPE_THRESHOLD_DP.dp.toPx() }
    val swipeState = remember { SwipeState(coroutineScope, swipeThresholdPx) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(swipeState.backgroundColor)
                .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            val currentCard = cards[swipeState.currentIndex]
            val nextCard = cards[(swipeState.currentIndex + 1) % cards.size]

            val progress = (swipeState.offsetX / swipeThresholdPx).coerceIn(-1f, 1f)
            val nextCardScale = 0.95f + 0.05f * progress.absoluteValue
            val nextCardAlpha = 0.5f + 0.5f * progress.absoluteValue

            swipeCard(
                card = nextCard,
                scale = nextCardScale,
                alpha = nextCardAlpha,
            )

            swipeCard(
                card = currentCard,
                offsetX = swipeState.offsetX,
                modifier =
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                if (!swipeState.isAnimating) {
                                    change.consumePositionChange()
                                    swipeState.offsetX += dragAmount.x
                                    swipeState.backgroundColor =
                                        when {
                                            swipeState.offsetX > 0 -> Color(0xFFE8F5E9)
                                            swipeState.offsetX < 0 -> Color(0xFFFFEBEE)
                                            else -> Color.White
                                        }
                                }
                            },
                            onDragEnd = {
                                if (swipeState.isAnimating) return@detectDragGestures

                                when {
                                    swipeState.offsetX > swipeThresholdPx ->
                                        swipeState.swipe(1, cards.size)

                                    swipeState.offsetX < -swipeThresholdPx ->
                                        swipeState.swipe(-1, cards.size)

                                    else -> {
                                        swipeState.offsetX = 0f
                                        swipeState.backgroundColor = Color.White
                                    }
                                }
                            },
                        )
                    },
            )
        }

        swipeButtons(
            onNope = { swipeState.swipe(-1, cards.size) },
            onLike = { swipeState.swipe(1, cards.size) },
        )
    }
}
