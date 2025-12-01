package com.pamn.ggmatch.app.architecture.swipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.swipe.model.ProfileCard
import kotlin.math.roundToInt

@Composable
fun swipeCard(
    card: ProfileCard,
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    alpha: Float = 1f,
    offsetX: Float = 0f,
) {
    Card(
        modifier =
            modifier
                .fillMaxSize()
                .scale(scale)
                .alpha(alpha)
                .offset { IntOffset(offsetX.roundToInt(), 0) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${card.name}, ${card.age}",
                style =
                    MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                modifier = Modifier.padding(top = 64.dp),
            )

            Text(
                text = card.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp),
            )

            Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Foto de perfil",
                tint = Color.Gray,
                modifier =
                    Modifier
                        .padding(top = 32.dp)
                        .size(240.dp),
            )
        }
    }
}
