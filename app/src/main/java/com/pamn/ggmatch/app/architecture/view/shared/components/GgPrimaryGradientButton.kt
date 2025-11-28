package com.pamn.ggmatch.app.architecture.view.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.architecture.view.shared.SharedColors
import com.pamn.ggmatch.app.architecture.view.shared.SharedDimens

@Composable
fun ggPrimaryGradientButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier =
            modifier
                .width(SharedDimens.buttonWidth)
                .height(SharedDimens.buttonHeight),
        shape = RoundedCornerShape(SharedDimens.buttonCornerRadius),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = SharedColors.primaryGradient,
                        shape = RoundedCornerShape(SharedDimens.buttonCornerRadius),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = SharedColors.primaryButtonText,
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black,
                    ),
            )
        }
    }
}
