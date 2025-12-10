package com.pamn.ggmatch.app.architecture.control.navBar

import androidx.navigation.NavHostController

class NavPresenter(
    private val navController: NavHostController,
) {
    fun onItemSelected(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            popUpTo("home") { saveState = true }
        }
    }
}
