package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pamn.ggmatch.app.architecture.control.swipe.view.swipeView
import com.pamn.ggmatch.app.architecture.view.auth.view.loginView
import com.pamn.ggmatch.app.architecture.view.auth.view.registerView
import com.pamn.ggmatch.app.architecture.view.testScreen.testView

@Composable
fun ggMatchNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Router.AUTH_LOGIN,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Router.AUTH_LOGIN) {
            loginView(
                onLoginSuccess = {
                    navController.navigate(Router.HOME) {
                        popUpTo(Router.AUTH_LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Router.AUTH_REGISTER) },
            )
        }

        composable(Router.AUTH_REGISTER) {
            registerView(
                onRegisterSuccess = {
                    navController.navigate(Router.HOME) {
                        popUpTo(Router.AUTH_REGISTER) { inclusive = true }
                    }
                },
                onGoToLogin = { navController.navigate(Router.AUTH_LOGIN) },
            )
        }

        composable(Router.HOME) {
            swipeView(
                presenter = AppContainer.presenter,
                profiles = AppContainer.profiles,
            )
        }

        composable(Router.PREFERENCES) {
            testView(Color.White)
        }

        composable(Router.CHAT) {
            testView(Color.Black)
        }

        composable(Router.PROFILE) {
            testView(Color.White)
        }
    }
}
