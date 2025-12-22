package com.pamn.ggmatch.app.architecture.view.matches.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.matches.MatchesContract
import com.pamn.ggmatch.app.architecture.control.matches.MatchesPresenter
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId

@Composable
fun matchesScreen(onBack: () -> Unit) {
    val profileRepository = AppContainer.profileRepository
    val swipeRepository = AppContainer.swipeInteractionsRepository
    val currentUserId = AppContainer.currentUserId

    var uiState by remember { mutableStateOf<MatchesUiState>(MatchesUiState.Loading) }
    var allProfiles by remember { mutableStateOf<List<UserProfile>>(emptyList()) }

    val presenter = remember { MatchesPresenter(repository = profileRepository) }

    val view =
        remember {
            object : MatchesContract.View {
                override fun showProfiles(profilesList: List<UserProfile>) {
                    allProfiles = profilesList
                }

                override fun showError(message: String) {
                    uiState = MatchesUiState.Error(message)
                }
            }
        }

    DisposableEffect(Unit) {
        presenter.attachView(view)
        presenter.loadProfiles()
        onDispose { presenter.detachView() }
    }

    LaunchedEffect(allProfiles) {
        if (allProfiles.isNotEmpty()) {
            uiState = MatchesUiState.Loading

            val matches =
                filterOnlyMatches(
                    allProfiles = allProfiles,
                    swipeRepository = swipeRepository,
                    currentUserId = currentUserId,
                )

            uiState =
                if (matches.isEmpty()) {
                    MatchesUiState.Empty
                } else {
                    MatchesUiState.Success(matches)
                }
        }
    }

    when (val state = uiState) {
        MatchesUiState.Loading -> loadingMatchesView()
        MatchesUiState.Empty -> emptyMatchesView(onBack)
        is MatchesUiState.Success -> matchesListView(profiles = state.profiles, onBack)
        is MatchesUiState.Error -> errorMatchesView(message = state.message, onBack)
    }
}

private suspend fun filterOnlyMatches(
    allProfiles: List<UserProfile>,
    swipeRepository: SwipeHistoryRepository,
    currentUserId: UserId,
): List<UserProfile> {
    val myHistory =
        (swipeRepository.get(currentUserId) as? com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok)
            ?.value ?: return emptyList()

    return allProfiles.filter { candidate ->
        val iLikedCandidate = myHistory.items[candidate.id]?.type == com.pamn.ggmatch.app.architecture.model.swipe.SwipeType.LIKE

        if (iLikedCandidate) {
            val candidateHistory =
                (swipeRepository.get(candidate.id) as? com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok)
                    ?.value
            val candidateLikedMe =
                candidateHistory?.items?.get(currentUserId)?.type ==
                    com.pamn.ggmatch.app.architecture.model.swipe.SwipeType.LIKE

            candidateLikedMe
        } else {
            false
        }
    }
}
