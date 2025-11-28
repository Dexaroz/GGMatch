// GgAuthHeader.kt
package com.pamn.ggmatch.app.architecture.view.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.ggmatch.app.architecture.view.auth.AuthColors
import com.pamn.ggmatch.app.architecture.view.auth.AuthDimens
import com.pamn.ggmatch.app.architecture.view.auth.AuthTextStyles

@Composable
fun GgAuthHeader(
    imageRes: Int,
    logoText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(AuthDimens.headerHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = AuthDimens.headerCornerRadius,
                        bottomEnd = AuthDimens.headerCornerRadius,
                    ),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = logoText,
            style = AuthTextStyles.logo,
            color = AuthColors.logoTextColor,
        )
    }
}
