package com.pamn.ggmatch.app.architecture.swipe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeCard

@Composable
fun SwipeView(
    modifier: Modifier = Modifier,
    profiles: List<Profile>,
    presenter: ProfilePresenterImplementation,
) {
    var currentIndex by remember { mutableStateOf(0) }
    val currentCard = profiles.getOrNull(currentIndex) ?: return

    var offsetX by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }
    var alpha by remember { mutableStateOf(1f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(currentIndex) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX > 200) { // swipe derecha
                            presenter.onNextClicked()
                            currentIndex = (currentIndex + 1) % profiles.size
                        } else if (offsetX < -200) { // swipe izquierda
                            presenter.onPreviousClicked()
                            currentIndex = if (currentIndex - 1 < 0) profiles.size - 1 else currentIndex - 1
                        }
                        offsetX = 0f
                        scale = 1f
                        alpha = 1f
                    },
                    onDrag = { _, dragAmount ->
                        val (dx, _) = dragAmount
                        offsetX += dx
                        scale = 1 - kotlin.math.abs(offsetX) / 2000f
                        alpha = 1 - kotlin.math.abs(offsetX) / 1000f
                    }
                )
            }
    ) {
        // Tarjeta principal
        swipeCard(
            card = currentCard,
            offsetX = offsetX,
            scale = scale,
            alpha = alpha
        )

        // Botones tipo Tinder
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                presenter.onPreviousClicked()
                currentIndex = if (currentIndex - 1 < 0) profiles.size - 1 else currentIndex - 1
            }) {
                Text("❌")
            }

            Button(onClick = {
                presenter.onNextClicked()
                currentIndex = (currentIndex + 1) % profiles.size
            }) {
                Text("⭐")
            }

            Button(onClick = {
                presenter.onNextClicked()
                currentIndex = (currentIndex + 1) % profiles.size
            }) {
                Text("✅")
            }
        }
    }
}

