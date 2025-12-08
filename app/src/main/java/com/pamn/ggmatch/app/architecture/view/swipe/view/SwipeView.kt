package com.pamn.ggmatch.app.architecture.control.swipe.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeActionButton
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeCard
import kotlin.math.abs

@Composable
fun swipeView(
    modifier: Modifier = Modifier,
    currentCard: Profile,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
) {
    var offsetX by remember(currentCard.id) { mutableStateOf(0f) }
    var scale by remember(currentCard.id) { mutableStateOf(1f) }
    var alpha by remember(currentCard.id) { mutableStateOf(1f) }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color(0xFF212121))
                .pointerInput(currentCard.id) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX > 200) {
                                onNext()
                            } else if (offsetX < -200) {
                                onNext()
                            }
                            offsetX = 0f
                            scale = 1f
                            alpha = 1f
                        },
                        onDrag = { _, dragAmount ->
                            val (dx, _) = dragAmount
                            offsetX += dx
                            scale = 1 - abs(offsetX) / 2500f
                            alpha = 1 - abs(offsetX) / 1200f
                        },
                    )
                },
    ) {
        swipeCard(
            card = currentCard,
            offsetX = offsetX,
            scale = scale,
            alpha = alpha,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 40.dp)
                    .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            swipeActionButton(
                iconRes = R.drawable.undo,
                iconTint = Color(0xFFFFBC03),
                backgroundColor = Color(0xFF474747),
                size = 60.dp,
                shape = RoundedCornerShape(22.dp),
            ) {
                onPrevious()
            }

            swipeActionButton(
                iconRes = R.drawable.no,
                iconTint = Color(0xFFFE4C6A),
                backgroundColor = Color(0xFF474747),
                size = 72.dp,
                shape = RoundedCornerShape(26.dp),
            ) {
                onNext()
            }

            swipeActionButton(
                iconRes = R.drawable.star,
                iconTint = Color(0xFF21BBFF),
                backgroundColor = Color(0xFF474747),
                size = 60.dp,
                shape = RoundedCornerShape(18.dp),
            ) {
                onNext()
            }

            swipeActionButton(
                iconRes = R.drawable.yes,
                iconTint = Color(0xFF44EAC5),
                backgroundColor = Color(0xFF474747),
                size = 72.dp,
                shape = RoundedCornerShape(26.dp),
            ) {
                onNext()
            }

            swipeActionButton(
                iconRes = R.drawable.lightning,
                iconTint = Color(0xFFAC52E6),
                backgroundColor = Color(0xFF474747),
                size = 60.dp,
                shape = RoundedCornerShape(20.dp),
            ) {
                onNext()
            }
        }
    }
}
