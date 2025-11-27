package com.pamn.ggmatch.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun GgMatchApp() {
    val navController = rememberNavController()
    GgMatchNavHost(navController = navController)
}