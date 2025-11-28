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

private val ScreenPadding = 24.dp
private val ButtonHeight = 50.dp
private val ButtonShape = RoundedCornerShape(24.dp)
private const val TITLEFONTSIZE = 28
private const val SUBTITLEFONTSIZE = 16

@Composable
fun test1Screen(
    onGoToTest2: () -> Unit,
    onGoToTest3: () -> Unit,
    onGoToTest4: () -> Unit,
) {
    baseTestScreen(
        title = "TEST 1",
        description = "Pantalla de prueba 1. Desde aquí puedes navegar al resto.",
    ) {
        primaryButton(text = "Ir a Test 2", onClick = onGoToTest2)
        Spacer(Modifier.height(12.dp))
        primaryButton(text = "Ir a Test 3", onClick = onGoToTest3)
        Spacer(Modifier.height(12.dp))
        primaryButton(text = "Ir a Test 4", onClick = onGoToTest4)
    }
}

@Composable
fun test2Screen(
    onBack: () -> Unit,
    onGoToTest3: () -> Unit,
) {
    baseTestScreen(
        title = "TEST 2",
        description = "Pantalla de prueba 2.",
    ) {
        primaryButton(text = "Volver", onClick = onBack)
        Spacer(Modifier.height(12.dp))
        primaryButton(text = "Ir a Test 3", onClick = onGoToTest3)
    }
}

@Composable
fun test3Screen(
    onBack: () -> Unit,
    onGoToTest4: () -> Unit,
) {
    baseTestScreen(
        title = "TEST 3",
        description = "Pantalla de prueba 3.",
    ) {
        primaryButton(text = "Volver", onClick = onBack)
        Spacer(Modifier.height(12.dp))
        primaryButton(text = "Ir a Test 4", onClick = onGoToTest4)
    }
}

@Composable
fun test4Screen(
    onBack: () -> Unit,
    onGoToTest1: () -> Unit,
) {
    baseTestScreen(
        title = "TEST 4",
        description = "Pantalla de prueba 4.",
    ) {
        primaryButton(text = "Volver", onClick = onBack)
        Spacer(Modifier.height(12.dp))
        primaryButton(text = "Ir a Test 1 (reset)", onClick = onGoToTest1)
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
                    .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = title,
                fontSize = TITLEFONTSIZE.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = description,
                fontSize = SUBTITLEFONTSIZE.sp,
            )

            Spacer(Modifier.height(16.dp))

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
                .height(ButtonHeight),
        shape = ButtonShape,
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
