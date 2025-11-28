package com.pamn.ggmatch.app.architecture.swipe.logic

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween

@Stable
class SwipeState(
    val coroutineScope: CoroutineScope,
    val swipeThresholdPx: Float
) {
    var currentIndex by mutableIntStateOf(0)
    var offsetX by mutableFloatStateOf(0f)
    var backgroundColor by mutableStateOf(Color.White)
    var isAnimating by mutableStateOf(false)

    fun nextCard(cardsSize: Int) {
        val nextIndex = (currentIndex + 1) % cardsSize
        currentIndex = if (nextIndex < 0) cardsSize - 1 else nextIndex
        offsetX = 0f
    }

    suspend fun handleAction(direction: Int, cardsSize: Int) {
        backgroundColor = if (direction > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
        nextCard(cardsSize)
        backgroundColor = Color.White
    }

    fun swipe(direction: Int, cardsSize: Int) {
        if (isAnimating) return
        coroutineScope.launch {
            isAnimating = true
            val target = direction * 2000f

            animate(
                initialValue = offsetX,
                targetValue = target,
                animationSpec = tween(250)
            ) { value, _ ->
                offsetX = value
                backgroundColor = when {
                    offsetX > 0 -> Color(0xFFE8F5E9)
                    offsetX < 0 -> Color(0xFFFFEBEE)
                    else -> Color.White
                }
            }

            handleAction(direction, cardsSize)
            offsetX = 0f
            backgroundColor = Color.White
            isAnimating = false
        }
    }
}
