package com.pamn.ggmatch.app.architecture.view.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pamn.ggmatch.app.architecture.view.auth.AuthColors
import com.pamn.ggmatch.app.architecture.view.auth.AuthTextStyles

@Composable
fun ggAuthTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AuthTextStyles.title,
        color = AuthColors.titleColor,
        modifier = modifier.fillMaxWidth(),
    )
}
