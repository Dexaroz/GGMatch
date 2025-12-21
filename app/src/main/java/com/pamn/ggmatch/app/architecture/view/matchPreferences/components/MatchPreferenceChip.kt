package com.pamn.ggmatch.app.architecture.view.matchPreferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.view.shared.SharedColors

@Composable
fun matchPreferenceChip(
    label: String,
    selected: Boolean,
    enabled: Boolean = true,
    iconRes: Int = R.drawable.chat,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)

    val background =
        if (selected) {
            SharedColors.primaryGradient
        } else {
            Brush.horizontalGradient(
                colors =
                    listOf(
                        Color(0x66EC1562),
                        Color(0x66770B93),
                    ),
            )
        }

    Row(
        modifier =
            Modifier
                .shadow(
                    elevation = if (selected) 8.dp else 2.dp,
                    shape = shape,
                    ambientColor = if (selected) Color(0x55E20A94) else Color.Black.copy(alpha = 0.1f),
                    spotColor = if (selected) Color(0xAA7A1FFF) else Color.Black.copy(alpha = 0.1f),
                )
                .background(background, shape)
                .clickable(enabled = enabled) { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
