package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pamn.ggmatch.app.architecture.model.profile.DummyProfileNavigator
import com.pamn.ggmatch.app.architecture.model.profile.preferences.*
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.view.auth.view.loginView
import com.pamn.ggmatch.app.architecture.view.auth.view.registerView
import com.pamn.ggmatch.app.architecture.view.preferences.view.preferencesScreen
import com.pamn.ggmatch.app.architecture.view.swipe.swipeScreen
import com.pamn.ggmatch.app.architecture.view.testScreen.testView
import com.pamn.ggmatch.app.architecture.io.profile.FirebaseProfileRepository
import com.pamn.ggmatch.app.architecture.control.profile.commands.UpsertUserProfileCommand
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ggMatchNavHost(navController: NavHostController) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val profileRepository = remember { FirebaseProfileRepository(firestore) }
    val timeProvider = remember { SystemTimeProvider() }
    val commandHandler = remember { UpsertUserProfileCommandHandler(profileRepository, timeProvider) }
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Router.HOME,
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

        // HOME mockup
        composable(Router.HOME) {
            val userId = remember { UserId("current_user_id_123") }
            val navigator = remember { DummyProfileNavigator() }
            swipeScreen(
                navigator = navigator,
                currentUserId = userId,
            )
        }

        // CHAT y PROFILE mockups
        composable(Router.CHAT) { testView(Color.Black) }
        composable(Router.PROFILE) { testView(Color.White) }

        // PREFERENCES
        composable(Router.PREFERENCES) {
            val userId = remember { UserId("current_user_id_123") }
            var loadedPreferences by remember { mutableStateOf<Preferences?>(null) }

            LaunchedEffect(userId) {
                when (val result = profileRepository.get(userId)) {
                    is Result.Ok -> {
                        val profile = result.value
                        loadedPreferences = profile?.preferences ?: Preferences(
                            favoriteRoles = setOf(LolRole.TOP),
                            languages = setOf(Language.SPANISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.ARAM)
                        )
                    }
                    is Result.Error -> {
                        loadedPreferences = Preferences(
                            favoriteRoles = setOf(LolRole.TOP),
                            languages = setOf(Language.SPANISH),
                            playSchedule = setOf(PlaySchedule.NIGHT),
                            playstyle = setOf(Playstyle.ARAM)
                        )
                    }
                }
            }

            loadedPreferences?.let { prefs ->
                preferencesScreen(
                    initial = prefs,
                    allRoles = LolRole.entries,
                    allLanguages = Language.entries,
                    allSchedules = PlaySchedule.entries,
                    allPlaystyles = Playstyle.entries,
                    onSave = { newPrefs ->
                        scope.launch {
                            commandHandler.handle(
                                UpsertUserProfileCommand(
                                    userId = userId,
                                    riotAccount = null, // no se cambia
                                    favoriteRoles = newPrefs.favoriteRoles,
                                    languages = newPrefs.languages,
                                    playSchedule = newPrefs.playSchedule,
                                    playstyle = newPrefs.playstyle
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
