package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pamn.ggmatch.app.architecture.view.Test1Screen
import com.pamn.ggmatch.app.architecture.view.Test2Screen
import com.pamn.ggmatch.app.architecture.view.Test3Screen
import com.pamn.ggmatch.app.architecture.view.Test4Screen

@Composable
fun GgMatchNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Router.TEST1,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Router.TEST1) {
            Test1Screen(
                onGoToTest2 = { navController.navigate(Router.TEST2) },
                onGoToTest3 = { navController.navigate(Router.TEST3) },
                onGoToTest4 = { navController.navigate(Router.TEST4) },
            )
        }

        composable(Router.TEST2) {
            Test2Screen(
                onBack = { navController.popBackStack() },
                onGoToTest3 = { navController.navigate(Router.TEST3) },
            )
        }

        composable(Router.TEST3) {
            Test3Screen(
                onBack = { navController.popBackStack() },
                onGoToTest4 = { navController.navigate(Router.TEST4) },
            )
        }

        composable(Router.TEST4) {
            Test4Screen(
                onBack = { navController.popBackStack() },
                onGoToTest1 = {
                    navController.navigate(Router.TEST1) {
                        popUpTo(0)
                    }
                },
            )
        }
    }
}