package com.pamn.ggmatch.app.architecture.view.swipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.control.swipe.view.swipeView // Importa la View Pura
import com.pamn.ggmatch.app.architecture.model.profile.Profile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator

class ComposeProfileViewImplementation(
    initialProfile: Profile,
) : ProfileView {
    // ⚠️ Estado que la UI de Compose leerá.
    val currentProfileState: MutableState<Profile> = mutableStateOf(initialProfile)

    override fun showProfile(profile: Profile) {
        // Cuando el Presenter notifica, actualizamos el estado.
        currentProfileState.value = profile
    }
}

@Composable
fun swipeScreen(navigator: ProfileNavigator) {
    // 1. Inicializa la View y el Presenter
    val initialProfile = navigator.current()
    val viewImplementation = remember { ComposeProfileViewImplementation(initialProfile) }
    val presenter = remember { ProfilePresenterImplementation(viewImplementation, navigator) }

    // 2. Observa el estado (el perfil actual)
    val currentCard = viewImplementation.currentProfileState.value

    // 3. Pasa el estado y las acciones a la View pura (SwipeView)
    swipeView(
        currentCard = currentCard,
        onNext = presenter::onNextClicked,
        onPrevious = presenter::onPreviousClicked,
    )
}
