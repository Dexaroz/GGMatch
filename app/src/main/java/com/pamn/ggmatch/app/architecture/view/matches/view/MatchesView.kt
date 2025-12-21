package com.pamn.ggmatch.app.architecture.view.matches.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.BACK_DESCRIPTION
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.MATCHES_TITLE
import com.pamn.ggmatch.app.architecture.view.matches.components.profileCardCompact

@Composable
fun matchesView(
    profiles: List<UserProfile>,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A)),
                        ),
                    ),
        )

        Image(
            painter = painterResource(id = R.drawable.twisted),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxSize()
                    .blur(40.dp)
                    .alpha(0.4f),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.undo),
                    contentDescription = BACK_DESCRIPTION,
                    tint = Color.White,
                    modifier =
                        Modifier
                            .size(28.dp)
                            .clickable { onBack() },
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = MATCHES_TITLE,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            if (profiles.isEmpty()) {
                EmptyMatchesView()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(profiles) { profile ->
                        profileCardCompact(profile = profile)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyMatchesView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No matches yet 💔",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Start swiping to find your perfect match!",
                color = Color.LightGray,
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}
