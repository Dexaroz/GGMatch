package com.pamn.ggmatch.app.architecture.view.matchFound.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R

@Composable
fun matchFoundView(
    characterImageId: Int,
    onGoToMatches: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color(0xFF0F0B18),
                                    Color(0xFF1E1A27),
                                    Color(0xFF2D2936),
                                ),
                        ),
                    ),
        )

        Image(
            painter = painterResource(id = characterImageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
                    .blur(40.dp),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = painterResource(id = R.drawable.match),
                contentDescription = "It's a match",
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(400.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(25.dp))

            Image(
                painter = painterResource(id = R.drawable.message),
                contentDescription = "Match subtitle",
                modifier =
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(200.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            Color(0xFFDBAC29).copy(alpha = 0.3f),
                                            Color.Transparent,
                                        ),
                                    radius = 80f,
                                ),
                            ),
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                ) {
                    Image(
                        painter = painterResource(R.drawable.go),
                        contentDescription = "Go to matches",
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color(0xFFDBAC29)),
                    )
                }
            }
        }
    }
}
