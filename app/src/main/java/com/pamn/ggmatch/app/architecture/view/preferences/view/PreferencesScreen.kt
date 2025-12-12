package com.pamn.ggmatch.app.architecture.view.preferences.view

import androidx.compose.runtime.*
import com.pamn.ggmatch.app.architecture.control.preferences.PreferencesContract
import com.pamn.ggmatch.app.architecture.control.preferences.PreferencesPresenter
import com.pamn.ggmatch.app.architecture.model.profile.preferences.*

@Composable
fun preferencesScreen(
    initial: Preferences,
    allRoles: List<LolRole>,
    allLanguages: List<Language>,
    allSchedules: List<PlaySchedule>,
    allPlaystyles: List<Playstyle>,
    onSave: (Preferences) -> Unit,
    onBack: () -> Unit,
) {
    val presenter = remember {
        PreferencesPresenter(
            initial = initial,
            onSave = onSave
        )
    }

    var uiState by remember { mutableStateOf(initial) }

    val view = remember {
        object : PreferencesContract.View {
            override fun showState(preferences: Preferences) {
                uiState = preferences
            }

            override fun showError(message: String) {
                // Snackbars si quieres
            }
        }
    }

    DisposableEffect(Unit) {
        presenter.attachView(view)

        onDispose {
            presenter.detachView()
        }
    }

    PreferencesView(
        uiState = uiState,
        allRoles = allRoles,
        allLanguages = allLanguages,
        allSchedules = allSchedules,
        allPlaystyles = allPlaystyles,
        presenter = presenter,
        onBack = onBack
    )
}
