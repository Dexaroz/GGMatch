package com.pamn.ggmatch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pamn.ggmatch.app.theme.ggMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            ggMatchTheme {
                ggMatchApp()
            }
        }
    }
}
