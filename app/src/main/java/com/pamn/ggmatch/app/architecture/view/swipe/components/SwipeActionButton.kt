package com.pamn.ggmatch.app.architecture.view.swipe.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun swipeActionButton(
    iconRes: Int,
    iconTint: Color,
    backgroundColor: Color = Color(0xFF1D1D1D),
    size: Dp = 70.dp,
    shape: Shape = RoundedCornerShape(22.dp),
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(size)
                .background(backgroundColor, shape)
                .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(iconTint),
            modifier = Modifier.size(size * 0.45f),
        )
    }
}
