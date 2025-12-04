package com.pamn.ggmatch.app.architecture.view.swipe.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.architecture.model.profile.Profile

@Composable
fun swipeCard(
    card: Profile,
    offsetX: Float = 0f,
    scale: Float = 1f,
    alpha: Float = 1f,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 50.dp)
                .graphicsLayer {
                    translationX = offsetX
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    rotationZ = offsetX / 30
                },
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black),
        ) {
            // 1. Imagen de Fondo
            Image(
                painter = painterResource(id = card.backgroundImageRes),
                contentDescription = "Background image for ${card.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            // 2. Degradado Inferior (para que el texto y el círculo se integren mejor)
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color(0xDD000000),
                                    ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY,
                            ),
                        ),
            )

            // 3. Contenido de Perfil (Imagen, Nombre, Descripción)
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Image(
                    painter = painterResource(id = card.imageRes),
                    contentDescription = "${card.name} profile picture",
                    modifier =
                        Modifier
                            .size(width = 250.dp, height = 350.dp)
                            .clip(RoundedCornerShape(200.dp))
                            .border(6.dp, Color.White, RoundedCornerShape(200.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = card.description,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
