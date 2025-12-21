package com.pamn.ggmatch.app

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.pamn.ggmatch.app.architecture.view.shared.navBar.navBarHost

@Composable
fun ggMatchApp() {
    val navController = rememberNavController()

    val currentRoute =
        navController
            .currentBackStackEntryFlow
            .collectAsState(initial = navController.currentBackStackEntry)
            .value?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute in listOf("home")) {
                navBarHost(navController)
            }
        },
    ) { padding ->
        ggMatchNavHost(navController)
    }
}
