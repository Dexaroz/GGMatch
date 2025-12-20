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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId

private fun userIdToConsistentInt(userId: UserId): Int {
    return (userId.hashCode() and 0x7FFFFFFF)
}

@Composable
fun swipeCard(
    card: UserProfile,
    offsetX: Float = 0f,
    scale: Float = 1f,
    alpha: Float = 1f,
) {
    val backgroundImages =
        listOf(
            R.drawable.jinx,
            R.drawable.thresh,
            R.drawable.twisted,
        )
    val numberOfBackgrounds = backgroundImages.size

    val profileIdInt = userIdToConsistentInt(card.id)
    val imageIndex = profileIdInt % numberOfBackgrounds
    val backgroundRes = backgroundImages[imageIndex]

    val defaultImageRes = R.drawable.profile_picture
    val gameName = card.riotAccount?.gameName ?: "Invocador Desconocido"
    val tagLine = card.riotAccount?.tagLine ?: "EUW"
    val mainRoles = card.preferences.favoriteRoles.joinToString(separator = ", ") { it.name }
    val languages = card.preferences.languages.joinToString(separator = ", ") { it.name.take(2) }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 100.dp)
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
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = "Background image for $gameName",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

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
                    painter = painterResource(id = defaultImageRes),
                    contentDescription = "$gameName profile picture",
                    modifier =
                        Modifier
                            .size(width = 250.dp, height = 350.dp)
                            .clip(RoundedCornerShape(200.dp))
                            .border(6.dp, Color.White, RoundedCornerShape(200.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = gameName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Roles: $mainRoles",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF44EAC5),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Idiomas: $languages",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
