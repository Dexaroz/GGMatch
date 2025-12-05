package com.pamn.ggmatch.app.architecture.model.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.pamn.ggmatch.app.Router

sealed class TopNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    object Home : TopNavItem(Router.HOME, "Home", Icons.Default.Home)

    object Preferences : TopNavItem(Router.PREFERENCES, "Chat", Icons.Default.FilterAlt)

    object Chats : TopNavItem(Router.CHAT, "Chat", Icons.Default.ChatBubble)

    object Profile : TopNavItem(Router.PROFILE, "Profile", Icons.Default.Person)
}

val topNavItems =
    listOf(
        TopNavItem.Home,
        TopNavItem.Preferences,
        TopNavItem.Chats,
        TopNavItem.Profile,
    )
