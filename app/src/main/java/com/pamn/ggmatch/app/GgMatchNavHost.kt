package com.pamn.ggmatch.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.control.matching.DummyProfileNavigator
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.UpsertUserProfileCommandHandler
import com.pamn.ggmatch.app.architecture.io.profile.FirebaseProfileRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.profile.DummyUserProfiles
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.SystemTimeProvider
import com.pamn.ggmatch.app.architecture.view.auth.view.loginView
import com.pamn.ggmatch.app.architecture.view.auth.view.registerView
import com.pamn.ggmatch.app.architecture.view.matchPreferences.view.preferencesScreen
import com.pamn.ggmatch.app.architecture.view.matches.view.profileListView
import com.pamn.ggmatch.app.architecture.view.swipe.swipeScreen
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

        // HOME con preferencias reales
        composable(Router.HOME) {
            // Estado para las preferencias del usuario
            val currentUserPreferences = remember { androidx.compose.runtime.mutableStateOf<MatchPreferences?>(null) }

            // Cargar preferencias desde Firebase
            LaunchedEffect(Unit) {
                when (val result = AppContainer.matchPreferencesRepository.get(AppContainer.currentUserId)) {
                    is Result.Ok -> currentUserPreferences.value = result.value
                    is Result.Error -> {
                        // manejar error según tu lógica, aquí solo logueamos
                        println("Error cargando preferencias: ${result.error}")
                    }
                }
            }

            // Si las preferencias están cargadas, crear el navigator
            currentUserPreferences.value?.let { prefs ->
                val navigator = remember { DummyProfileNavigator(currentUserPreferences = prefs) }
                swipeScreen(navigator = navigator)
            } ?: run {
                loadingScreen()
            }
        }

        // MATCHES y PROFILE mockups
        composable(Router.MATCHES) {
            profileListView(
                profiles = DummyUserProfiles.all,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Router.PROFILE) { testView(Color.White) }

        // PREFERENCES
        composable(Router.PREFERENCES) {
            preferencesScreen(
                allRoles = LolRole.entries.toList(),
                allLanguages = Language.entries.toList(),
                allSchedules = PlaySchedule.entries.toList(),
                allPlaystyles = Playstyle.entries.toList(),
                onBack = { navController.popBackStack() },
            )
        }
    }
}

// Ejemplo simple de pantalla de carga
@Composable
fun loadingScreen() {
    androidx.compose.material3.Text(text = "Cargando preferencias...", color = Color.Gray)
}
