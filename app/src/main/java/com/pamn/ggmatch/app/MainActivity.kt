package com.pamn.ggmatch.app

import SwipeDemo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pamn.ggmatch.app.theme.ggMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // El setContent debe ser llamado una única vez al final de onCreate.
        setContent {
            ggMatchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llama al Composable de la demo de deslizamiento (swipe).
                    SwipeDemo(
                        // Pasar el padding del Scaffold es buena práctica,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}