package com.pamn.ggmatch.app.architecture.view.swipe.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import kotlin.math.roundToInt

@Composable
fun swipeCard(card: Profile, offsetX: Float = 0f, scale: Float = 1f, alpha: Float = 1f) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .scale(scale)
            .alpha(alpha)
            .offset { IntOffset(offsetX.roundToInt(), 0) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Foto circular del perfil
            Image(
                painter = painterResource(id = card.imageRes),
                contentDescription = "${card.name} profile picture",
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = card.name,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Descripción
            Text(
                text = card.description,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
