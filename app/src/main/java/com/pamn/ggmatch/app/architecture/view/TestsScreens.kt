package com.pamn.ggmatch.app.architecture.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.app.architecture.swipe.swipeDemoScreen

private val SCREEN_PADDING = 24.dp
private val BUTTON_HEIGHT = 50.dp
private val BUTTON_SHAPE = RoundedCornerShape(24.dp)
private const val TITLE_FONT_SIZE = 28
private const val SUBTITLE_FONT_SIZE = 16

@Composable
fun testSwipeScreen(onBack: () -> Unit) {
    baseTestScreen(
        title = "TEST SWIPE",
        description = "Pantalla de prueba del Swipe Card.",
    ) {
        swipeDemoScreen(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(500.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        primaryButton(
            text = "Volver",
            onClick = onBack,
        )
    }
}

@Composable
private fun baseTestScreen(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(SCREEN_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = title,
                fontSize = TITLE_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = description,
                fontSize = SUBTITLE_FONT_SIZE.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
    }
}

@Composable
private fun primaryButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT),
        shape = BUTTON_SHAPE,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}
