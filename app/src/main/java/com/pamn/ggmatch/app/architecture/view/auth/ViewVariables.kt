package com.pamn.ggmatch.app.architecture.view.auth

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AuthDimens {
    val titleTopMargin = 24.dp
    val fieldVerticalSpacing = 16.dp
    val buttonTopMargin = 32.dp
    val logoTextSize = 55.sp
    val titleTextSize = 28.sp
    val headerCornerRadius = 25.dp
    val headerHeight = 400.dp
}

object AuthColors {
    val titleColor = Color(0xFF000000)
    val logoTextColor = Color(0xFFE3AC28)
}

object AuthTextStyles {
    val title =
        TextStyle(
            fontSize = AuthDimens.titleTextSize,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )

    val logo =
        TextStyle(
            fontSize = AuthDimens.logoTextSize,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp,
        )
}
