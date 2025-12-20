package com.pamn.ggmatch.app.architecture.view.matchPreferences.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.matchPreferences.MatchPreferencesContract
import com.pamn.ggmatch.app.architecture.control.matchPreferences.MatchPreferencesPresenter
import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Language
import com.pamn.ggmatch.app.architecture.model.profile.preferences.LolRole
import com.pamn.ggmatch.app.architecture.model.profile.preferences.PlaySchedule
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Playstyle

@Composable
fun preferencesScreen(
    allRoles: List<LolRole>,
    allLanguages: List<Language>,
    allSchedules: List<PlaySchedule>,
    allPlaystyles: List<Playstyle>,
    onBack: () -> Unit,
) {
    val userId = AppContainer.currentUserId
    val repository = AppContainer.matchPreferencesRepository
    val handler = AppContainer.upsertMatchPreferencesHandler

    val presenter =
        remember {
            MatchPreferencesPresenter(
                userId = userId,
                repository = repository,
                upsertMatchPreferencesHandler = handler,
                onSaveSuccess = onBack,
            )
        }

    var uiState by remember { mutableStateOf(Preferences.default()) }

    val view =
        remember {
            object : MatchPreferencesContract.View {
                override fun showState(preferences: Preferences) {
                    uiState = preferences
                }

                override fun showError(message: String) {
                }
            }
        }

    DisposableEffect(Unit) {
        presenter.attachView(view)
        onDispose {
            presenter.detachView()
        }
    }

    preferencesView(
        uiState = uiState,
        allRoles = allRoles,
        allLanguages = allLanguages,
        allSchedules = allSchedules,
        allPlaystyles = allPlaystyles,
        presenter = presenter,
        onBack = {
            presenter.save()
        },
    )
}
