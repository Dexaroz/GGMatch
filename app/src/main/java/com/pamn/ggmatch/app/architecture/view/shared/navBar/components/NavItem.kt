package com.pamn.ggmatch.app.architecture.view.shared.navBar.components

import androidx.annotation.DrawableRes
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.Router

sealed class TopNavItem(
    val route: String,
    val label: String,
    @DrawableRes val iconRes: Int,
) {
    object Home : TopNavItem(Router.HOME, "Home", R.drawable.home)

    object Preferences : TopNavItem(Router.PREFERENCES, "Preferences", R.drawable.preferences)

    object Chats : TopNavItem(Router.CHATS, "Chats", R.drawable.chat)

    object Profile : TopNavItem(Router.PROFILE, "Profile", R.drawable.profile)
}

val topNavItems =
    listOf(
        TopNavItem.Home,
        TopNavItem.Preferences,
        TopNavItem.Chats,
        TopNavItem.Profile,
    )
