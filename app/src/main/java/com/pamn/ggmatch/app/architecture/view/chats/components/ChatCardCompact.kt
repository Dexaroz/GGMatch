package com.pamn.ggmatch.app.architecture.view.chats.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.chats.ChatTextVariables.DEFAULT_SERVER
import com.pamn.ggmatch.app.architecture.view.chats.ChatTextVariables.LAST_MESSAGE_FALLBACK
import com.pamn.ggmatch.app.architecture.view.chats.ChatTextVariables.PROFILE_PICTURE_DESCRIPTION_SUFFIX
import com.pamn.ggmatch.app.architecture.view.chats.ChatTextVariables.UNKNOWN_SUMMONER

@Composable
fun chatCardCompact(
    profile: UserProfile,
    lastMessage: String?,
    onClick: () -> Unit,
) {
    val gameName = profile.riotAccount?.gameName ?: UNKNOWN_SUMMONER
    val gameTag = profile.riotAccount?.tagLine ?: DEFAULT_SERVER

    val photoUrl: String? = profile.photoUrl?.value

    val avatarPainter =
        if (!photoUrl.isNullOrBlank()) {
            rememberAsyncImagePainter(model = photoUrl)
        } else {
            painterResource(id = R.drawable.profile_picture)
        }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF1E1E1E))
                    .padding(8.dp),
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = "$gameName $PROFILE_PICTURE_DESCRIPTION_SUFFIX",
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
                    text = "$gameName#$gameTag",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = (lastMessage?.takeIf { it.isNotBlank() } ?: LAST_MESSAGE_FALLBACK),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}