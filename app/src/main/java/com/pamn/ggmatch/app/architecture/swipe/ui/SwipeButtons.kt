package com.pamn.ggmatch.app.architecture.swipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R

@Composable
fun SwipeButtons(
    onNope: () -> Unit,
    onLike: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconButton(
            onClick = onNope,
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFFFEBEE), MaterialTheme.shapes.extraLarge),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.no),
                contentDescription = "Nope",
                tint = Color(0xFFF44336),
                modifier = Modifier.size(36.dp),
            )
        }

        IconButton(
            onClick = onLike,
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFE8F5E9), MaterialTheme.shapes.extraLarge),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.yes),
                contentDescription = "Like",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(36.dp),
            )
        }
    }
}
