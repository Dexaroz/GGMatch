package com.pamn.ggmatch.app.architecture.view.matches.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.matches.MatchesContract
import com.pamn.ggmatch.app.architecture.control.matches.MatchesPresenter
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile

@Composable
fun profileListScreen(onBack: () -> Unit) {
    val repository = AppContainer.profileRepository

    val presenter =
        remember {
            MatchesPresenter(
                repository = repository,
                onBack = onBack,
            )
        }

    var profiles by remember { mutableStateOf<List<UserProfile>>(emptyList()) }

    val view =
        remember {
            object : MatchesContract.View {
                override fun showProfiles(profilesList: List<UserProfile>) {
                    profiles = profilesList
                }

                override fun showError(message: String) {
                    // handle error (toast/snackbar)
                }
            }
        }

    DisposableEffect(Unit) {
        presenter.attachView(view)
        presenter.loadProfiles()
        onDispose { presenter.detachView() }
    }

    profileListView(
        profiles = profiles,
        onBack = onBack,
    )
}
