package com.pamn.ggmatch.app.architecture.view.navBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pamn.ggmatch.app.architecture.control.navBar.NavPresenter
import com.pamn.ggmatch.app.architecture.model.navigation.topNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navBar(navController: NavHostController) {
    val presenter = remember { NavPresenter(navController) }

    val currentRoute =
        navController.currentBackStackEntryFlow
            .collectAsState(initial = navController.currentBackStackEntry)
            .value?.destination?.route

    TopAppBar(
        title = {
            // Espaciado equitativo
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                topNavItems.forEach { item ->

                    Button(
                        onClick = { presenter.onItemSelected(item.route) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint =
                                if (currentRoute == item.route) {
                                    Color.Red
                                } else {
                                    Color.Black
                                },
                        )
                    }
                }
            }
        },
        navigationIcon = {},
        actions = {},
    )
}
