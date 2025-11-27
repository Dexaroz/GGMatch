package com.pamn.ggmatch.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ggMatchApp() {
    val navController = rememberNavController()
    ggMatchNavHost(navController = navController)
}
