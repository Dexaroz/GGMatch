package com.pamn.ggmatch.app

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pamn.ggmatch.R
import com.pamn.ggmatch.app.architecture.view.auth.view.LoginView
import com.pamn.ggmatch.app.architecture.view.test1Screen
import com.pamn.ggmatch.app.architecture.view.test2Screen
import com.pamn.ggmatch.app.architecture.view.test3Screen
import com.pamn.ggmatch.app.architecture.view.test4Screen
import kotlinx.coroutines.CompletableDeferred

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

            val context = LocalContext.current

            val gso = remember {
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .build()
            }
            val googleClient = remember { GoogleSignIn.getClient(context, gso) }

            var currentDeferred by remember { mutableStateOf<CompletableDeferred<String?>?>(null) }

            val googleLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val token =
                        if (result.resultCode != Activity.RESULT_OK) {
                            null
                        } else {
                            runCatching {
                                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                                val account = task.getResult(ApiException::class.java)
                                account.idToken
                            }.getOrNull()
                        }

                    currentDeferred?.complete(token)
                    currentDeferred = null
                }

            LoginView(
                onLoginSuccess = {
                    navController.navigate(Router.HOME) {
                        popUpTo(Router.AUTH_LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Router.AUTH_REGISTER) },

                onRequestGoogleIdToken = {
                    val deferred = CompletableDeferred<String?>()
                    currentDeferred = deferred
                    googleLauncher.launch(googleClient.signInIntent)
                    deferred.await()
                },
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
