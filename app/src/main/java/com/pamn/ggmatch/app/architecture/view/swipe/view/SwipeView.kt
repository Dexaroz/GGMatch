package com.pamn.ggmatch.app.architecture.control.swipe.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar // ⭐️ NUEVO: Import para mostrar el mensaje de error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeActionButton
import com.pamn.ggmatch.app.architecture.view.swipe.components.swipeCard
import kotlin.math.abs
@Composable
fun swipeView(
    modifier: Modifier = Modifier,
    currentCard: UserProfile,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    errorMessage: String?,
) {
    var offsetX by remember(currentCard.id) { mutableStateOf(0f) }
    var scale by remember(currentCard.id) { mutableStateOf(1f) }
    var alpha by remember(currentCard.id) { mutableStateOf(1f) }

    // Contenedor principal para capas de fondo + contenido
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(currentCard.id) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX > 200) onLike()
                        else if (offsetX < -200) onDislike()
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
        // --- 1. FONDO (Igual al anterior) ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A))
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.twisted), // Tu imagen decorativa
            contentDescription = null,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(40.dp)
                .alpha(0.4f)
        )

        // --- 2. CONTENIDO ---
        swipeCard(
            card = currentCard,
            offsetX = offsetX,
            scale = scale,
            alpha = alpha,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            swipeActionButton(
                iconRes = R.drawable.no,
                iconTint = Color(0xFFFE4C6A),
                backgroundColor = Color.White.copy(alpha = 0.1f),
                size = 72.dp,
                shape = RoundedCornerShape(26.dp),
            ) {
                onDislike()
            }

            swipeActionButton(
                iconRes = R.drawable.yes,
                iconTint = Color(0xFF44EAC5),
                backgroundColor = Color.White.copy(alpha = 0.1f),
                size = 72.dp,
                shape = RoundedCornerShape(26.dp),
            ) {
                onLike()
            }
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
            ) {
                Snackbar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color(0xFFFE4C6A),
                    content = { androidx.compose.material3.Text(errorMessage) },
                )
            }
        }
    }
}