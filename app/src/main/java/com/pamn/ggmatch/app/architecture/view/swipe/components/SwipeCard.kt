package com.pamn.ggmatch.app.architecture.view.swipe.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.LANGUAGES_PREFIX
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.ROLES_PREFIX
import com.pamn.ggmatch.app.architecture.view.swipe.SwipeTextVariables.UNKNOWN_NAME
import kotlin.math.roundToInt

private fun userIdToConsistentInt(userId: UserId): Int = (userId.hashCode() and 0x7FFFFFFF)

private fun tierToDrawableRes(tier: String): Int {
    return when (tier.trim().uppercase()) {
        "IRON" -> R.drawable.iron
        "BRONZE" -> R.drawable.bronze
        "SILVER" -> R.drawable.silver
        "GOLD" -> R.drawable.gold
        "PLATINUM" -> R.drawable.platinum
        "EMERALD" -> R.drawable.emerald
        "DIAMOND" -> R.drawable.diamond
        "MASTER" -> R.drawable.master
        "GRANDMASTER" -> R.drawable.grandmaster
        "CHALLENGER" -> R.drawable.challenger
        else -> R.drawable.profile_picture
    }
}

@Composable
fun swipeCard(
    card: UserProfile,
    offsetX: Float = 0f,
    scale: Float = 1f,
    alpha: Float = 1f,
) {
    var flipped by remember(card.id) { mutableStateOf(false) }

    val flipRotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "flipRotation",
    )

    val showBack = flipRotation >= 90f

    val density = LocalDensity.current
    val cameraDistancePx = with(density) { 18.dp.toPx() } * 10f

    val backgroundImages =
        listOf(
            R.drawable.jinx,
            R.drawable.thresh,
            R.drawable.twisted,
        )

    val profileIdInt = userIdToConsistentInt(card.id)
    val backgroundRes = backgroundImages[profileIdInt % backgroundImages.size]

    val gameName = card.username ?: UNKNOWN_NAME
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

                    rotationY = flipRotation
                    cameraDistance = cameraDistancePx
                }
                .clickable { flipped = !flipped },
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        if (!showBack) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
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
                                    colors = listOf(Color.Transparent, Color(0xDD000000)),
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
                    val context = LocalContext.current
                    val photoUrl = card.photoUrl?.value

                    Box(
                        modifier =
                            Modifier
                                .size(width = 250.dp, height = 350.dp)
                                .clip(RoundedCornerShape(200.dp))
                                .border(6.dp, Color.White, RoundedCornerShape(200.dp))
                                .background(Color.Black),
                    ) {
                        AsyncImage(
                            model =
                                ImageRequest.Builder(context)
                                    .data(photoUrl)
                                    .crossfade(true)
                                    .build(),
                            placeholder = painterResource(R.drawable.profile_picture),
                            error = painterResource(R.drawable.profile_picture),
                            contentDescription = "$gameName profile picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = gameName.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$ROLES_PREFIX$mainRoles",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF44EAC5),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$LANGUAGES_PREFIX$languages",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                        .background(Color.Black),
            ) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xAA000000)),
                )

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val riot = card.riotAccount

                    Text(
                        text = riot?.let { "${it.gameName}#${it.tagLine}" } ?: "No Riot account",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(18.dp))

                    val soloq = riot?.soloq
                    if (soloq == null) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x22000000),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Sin SoloQ stats todavía.",
                                color = Color.LightGray,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = tierToDrawableRes(soloq.tier)),
                            contentDescription = soloq.tier,
                            modifier = Modifier.size(300.dp),
                        )

                        Text(
                            text =
                                buildString {
                                    append(soloq.tier)
                                    if (!soloq.division.isNullOrBlank()) append(" ${soloq.division}")
                                },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "${soloq.leaguePoints} LP",
                            color = Color(0xFF44EAC5),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.height(16.dp))

                        val total = (soloq.wins + soloq.losses).coerceAtLeast(1)
                        val wr = ((soloq.wins.toDouble() / total.toDouble()) * 100.0).roundToInt()

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x22000000),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "W/L: ${soloq.wins}W • ${soloq.losses}L",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                )
                                Text(
                                    text = "Winrate: $wr%",
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                )
                                if (soloq.hotStreak) {
                                    Text(
                                        text = "🔥 Hot streak",
                                        color = Color(0xFFFFC107),
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                if (soloq.inactive) {
                                    Text(
                                        text = "⏸️ Inactive",
                                        color = Color(0xFFFE4C6A),
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0x22FFFFFF),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Tap to return",
                            modifier = Modifier.padding(12.dp),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}
