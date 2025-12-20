package com.pamn.ggmatch.app.architecture.view.matches.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.DEFAULT_SERVER
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.LANGUAGES_PREFIX
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.ROLES_PREFIX
import com.pamn.ggmatch.app.architecture.view.matches.MatchesTextVariables.UNKNOWN_SUMMONER

@Composable
fun profileCardCompact(profile: UserProfile) {
    val defaultImageRes = R.drawable.profile_picture

    // Uso de constantes actualizadas
    val gameName = profile.riotAccount?.gameName ?: UNKNOWN_SUMMONER
    val tagLine = profile.riotAccount?.tagLine ?: DEFAULT_SERVER

    val mainRoles = profile.preferences.favoriteRoles.joinToString(", ") { it.name }
    val languages = profile.preferences.languages.joinToString(", ") { it.name.take(2) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E))
                    .padding(8.dp),
        ) {
            Image(
                painter = painterResource(id = defaultImageRes),
                contentDescription = "$gameName profile picture",
                modifier =
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(
                    text = gameName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    // Uso de constante actualizada
                    text = "$ROLES_PREFIX$mainRoles",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF44EAC5),
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    // Uso de constante actualizada
                    text = "$LANGUAGES_PREFIX$languages",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                )
            }
        }
    }
}
