package com.pamn.ggmatch.app.architecture.view.profiles

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.app.architecture.view.shared.SharedColors

object ProfileDimens {
    val screenPadding = 16.dp
    val sectionSpacing = 14.dp
    val headerSpacing = 12.dp

    val avatarSize = 88.dp
    val headerCornerRadius = 25.dp
    val headerHeight = 260.dp

    val fieldSpacing = 10.dp
    val buttonTopSpacing = 12.dp
}

object ProfileColors {
    val headerOverlay =
        Brush.verticalGradient(
            colors = listOf(Color(0x00000000), Color(0x66000000)),
        )

    val titleColor = Color(0xFFFFFFFF)
    val subtitleColor = Color(0xCCFFFFFF)

    val primaryGradient = SharedColors.primaryGradient
}

object ProfileTextStyles {
    val headerTitle =
        androidx.compose.ui.text.TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ProfileColors.titleColor,
        )

    val headerSubtitle =
        androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = ProfileColors.subtitleColor,
        )

    val sectionTitle =
        androidx.compose.ui.text.TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
}
