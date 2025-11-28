package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pamn.ggmatch.app.architecture.view.auth.view.loginView
import com.pamn.ggmatch.app.architecture.view.test1Screen
import com.pamn.ggmatch.app.architecture.view.test2Screen
import com.pamn.ggmatch.app.architecture.view.test3Screen
import com.pamn.ggmatch.app.architecture.view.test4Screen

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

        composable(Router.HOME) {
            test1Screen(
                onGoToTest2 = { navController.navigate(Router.TEST2) },
                onGoToTest3 = { navController.navigate(Router.TEST3) },
                onGoToTest4 = { navController.navigate(Router.TEST4) },
            )
        }

        composable(Router.TEST2) {
            test2Screen(
                onBack = { navController.popBackStack() },
                onGoToTest3 = { navController.navigate(Router.TEST3) },
            )
        }

        composable(Router.TEST3) {
            test3Screen(
                onBack = { navController.popBackStack() },
                onGoToTest4 = { navController.navigate(Router.TEST4) },
            )
        }

        composable(Router.TEST4) {
            test4Screen(
                onBack = { navController.popBackStack() },
                onGoToTest1 = {
                    navController.navigate(Router.HOME) {
                        popUpTo(0)
                    }
                },
            )
        }
    }
}
