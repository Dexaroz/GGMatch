package com.pamn.ggmatch.app.architecture.view.shared.navBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pamn.ggmatch.app.architecture.control.navBar.NavPresenter
import com.pamn.ggmatch.app.architecture.model.navigation.topNavItems

@Composable
fun navBarHost(navController: NavHostController) {
    val presenter = remember { NavPresenter(navController) }

    val currentRoute =
        navController.currentBackStackEntryFlow
            .collectAsState(initial = navController.currentBackStackEntry)
            .value?.destination?.route

    navBar(
        currentRoute = currentRoute,
        onItemClick = { route -> presenter.onItemSelected(route) },
    )
}

@Composable
fun navBar(
    currentRoute: String?,
    onItemClick: (route: String) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
    ) {
        topNavItems.forEach { item ->
            val isSelected = currentRoute == item.route

            IconButton(
                onClick = { onItemClick(item.route) },
            ) {
                Image(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    modifier = Modifier.size(32.dp),
                    colorFilter =
                        ColorFilter.tint(
                            if (isSelected) Color(0xFFFD5068) else Color(0xFFB0B0B0),
                        ),
                )
            }
        }
    }
}
