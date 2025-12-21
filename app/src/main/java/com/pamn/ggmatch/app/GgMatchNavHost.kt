package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.control.matching.navigator.ProfileNavigatorImplementation
import com.pamn.ggmatch.app.architecture.control.matching.tools.ProfileFilter
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler
import com.pamn.ggmatch.app.architecture.io.profile.FirebaseProfileRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.view.auth.view.loginView
import com.pamn.ggmatch.app.architecture.view.auth.view.registerView
import com.pamn.ggmatch.app.architecture.view.matchPreferences.view.preferencesScreen
import com.pamn.ggmatch.app.architecture.view.matches.view.matchesScreen
import com.pamn.ggmatch.app.architecture.view.profile.view.profileEditView
import com.pamn.ggmatch.app.architecture.view.swipe.swipeScreen
import com.pamn.ggmatch.app.architecture.view.testScreen.RiotVerifyTestView
import com.pamn.ggmatch.app.architecture.view.testScreen.testView

@Composable
fun ggMatchNavHost(navController: NavHostController) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val profileRepository = remember { FirebaseProfileRepository(firestore) }
    val timeProvider = remember { SystemTimeProvider() }
    val commandHandler = remember { UpsertUserProfileCommandHandler(profileRepository, timeProvider) }
    val scope = rememberCoroutineScope()

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
            val currentUserPreferences = remember { mutableStateOf<MatchPreferences?>(null) }

            LaunchedEffect(Unit) {
                val userId = AppContainer.currentUserId
                val result = AppContainer.matchPreferencesRepository.get(userId)

                when (result) {
                    is Result.Ok -> {
                        if (result.value != null) {
                            currentUserPreferences.value = result.value
                        } else {
                            val defaultPrefs =
                                MatchPreferences.createNew(
                                    userId = userId,
                                    timeProvider = AppContainer.timeProvider,
                                )
                            currentUserPreferences.value = defaultPrefs
                            AppContainer.matchPreferencesRepository.addOrUpdate(defaultPrefs)
                        }
                    }
                    is Result.Error -> {
                        println("Error de red: ${result.error}")
                    }
                }
            }

            val prefs = currentUserPreferences.value

            if (prefs != null) {
                val navigator =
                    remember(prefs.id) {
                        ProfileNavigatorImplementation(
                            repository = AppContainer.profileRepository,
                            profileFilter = ProfileFilter(),
                            preferences = prefs,
                            swipeRepository = AppContainer.swipeInteractionsRepository,
                        )
                    }
                swipeScreen(navigator = navigator, navController = navController)
            } else {
                loadingScreen()
            }
        }

        composable(Router.MATCHES) {
            matchesScreen(
                onBack = {
                    navController.navigate(Router.HOME)
                }
            )
        }

        composable(Router.PREFERENCES) {
            preferencesScreen(
                allRoles = LolRole.entries.toList(),
                allLanguages = Language.entries.toList(),
                allSchedules = PlaySchedule.entries.toList(),
                allPlaystyles = Playstyle.entries.toList(),
                onBack = { navController.popBackStack() },
            )
        }

        composable(Router.PROFILE) {
            profileEditView(
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
fun loadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF322E3D), Color(0xFF1A1A1A)),
                        ),
                    ),
        )

        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color(0xFFFFFFFF),
            strokeWidth = 5.dp,
        )
    }
}
