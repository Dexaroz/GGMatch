package com.pamn.ggmatch.app.architecture.view.shared

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object SharedDimens {
    val screenHorizontalPadding = 24.dp
    val textFieldCornerRadius = 12.dp
    val buttonCornerRadius = 12.dp
    val buttonHeight = 52.dp
    val buttonWidth = 200.dp
}

object SharedColors {
    val primaryGradient =
        Brush.horizontalGradient(
            listOf(
                Color(0xFFE20A94),
                Color(0xFF7A1FFF),
            ),
        )

    val primaryButtonText = Color.White
}
