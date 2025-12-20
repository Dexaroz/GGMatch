package com.pamn.ggmatch.app.architecture.view.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.AppContainer.currentUserId
import com.pamn.ggmatch.app.architecture.control.matching.ProfileNavigator
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.NextProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.SwipeProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.view.swipeView
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

class ComposeProfileViewImplementation(
    initialProfile: UserProfile?,
) : ProfileView {
    val currentProfileState: MutableState<UserProfile?> = mutableStateOf(initialProfile)
    val errorState: MutableState<String?> = mutableStateOf(null)

    // Estado clave para la pantalla de agotamiento
    val isDeckEmptyState: MutableState<Boolean> = mutableStateOf(initialProfile == null)

    // showProfile ahora acepta nulo
    override fun showProfile(profile: UserProfile?) {
        if (profile == null) {
            currentProfileState.value = null
            isDeckEmptyState.value = true // Activar la pantalla de agotamiento
        } else {
            currentProfileState.value = profile
            isDeckEmptyState.value = false
        }
        errorState.value = null
    }

    override fun showError(message: String) {
        // Limpiar el perfil y desactivar el estado de agotamiento al mostrar un error
        currentProfileState.value = null
        isDeckEmptyState.value = false
        errorState.value = message
    }
}

// COMPOSABLE PRINCIPAL
@Composable
fun swipeScreen(
    navigator: ProfileNavigator,
    swipeInteractionsRepository: SwipeHistoryRepository = AppContainer.swipeInteractionsRepository,
) {
    val scope = rememberCoroutineScope()
    val initialProfile = navigator.current()

    val view =
        remember {
            ComposeProfileViewImplementation(initialProfile)
        }

    val nextProfileCommandHandler =
        remember {
            NextProfileCommandHandler(navigator)
        }

    val swipeProfileCommandHandler =
        remember {
            SwipeProfileCommandHandler(
                repository = swipeInteractionsRepository,
            )
        }

    val presenter =
        remember {
            ProfilePresenterImplementation(
                view = view,
                nextProfileCommandHandler = nextProfileCommandHandler,
                swipeProfileCommandHandler = swipeProfileCommandHandler,
                scope = scope,
                currentProfile = initialProfile,
                currentUserId = currentUserId,
            ).also { it.init() }
        }

    // LÓGICA DE VISUALIZACIÓN CONDICIONAL
    when {
        view.isDeckEmptyState.value -> {
            emptyProfilesView(
                message = "Agotamiento de Perfiles. No hay perfiles con sus preferencias actualmente disponibles.",
                suggestion = "Por favor, espere nuevos usuarios o amplíe sus preferencias de emparejamiento.",
            )
        }
        view.currentProfileState.value != null -> {
            val currentProfile = view.currentProfileState.value!!
            swipeView(
                currentCard = currentProfile,
                // ✅ onNext ha sido eliminado, ya que la navegación es gestionada por onLike/onDislike
                onLike = { presenter.onLikeClicked(currentProfile) },
                onDislike = { presenter.onDislikeClicked(currentProfile) },
                errorMessage = view.errorState.value,
            )
        }
        view.errorState.value != null -> {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("ERROR: ${view.errorState.value}", color = Color.Red, textAlign = TextAlign.Center)
            }
        }
        else -> {
            // Estado de carga o inicialización
        }
    }
}

// COMPONENTE PARA PERFILES AGOTADOS
@Composable
fun emptyProfilesView(
    message: String,
    suggestion: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFF212121)),
        // Fondo oscuro
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "⚠️ $message",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                text = suggestion,
                color = Color.LightGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}
