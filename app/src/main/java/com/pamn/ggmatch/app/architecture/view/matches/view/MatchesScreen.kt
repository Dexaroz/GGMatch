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
import kotlinx.coroutines.launch

@Composable
fun matchesScreen(onBack: () -> Unit) {
    val profileRepository = AppContainer.profileRepository
    val swipeRepository = AppContainer.swipeInteractionsRepository
    val currentUserId = AppContainer.currentUserId

    var profiles by remember { mutableStateOf<List<UserProfile>>(emptyList()) }

    val presenter = remember {
        MatchesPresenter(
            repository = profileRepository,
        )
    }

    val view = remember {
        object : MatchesContract.View {
            override fun showProfiles(profilesList: List<UserProfile>) {
                kotlinx.coroutines.MainScope().launch {
                    val matchedProfiles = filterOnlyMatches(
                        allProfiles = profilesList,
                        swipeRepository = swipeRepository,
                        currentUserId = currentUserId
                    )
                    profiles = matchedProfiles
                }
            }

            override fun showError(message: String) { }
        }
    }

    DisposableEffect(Unit) {
        presenter.attachView(view)
        presenter.loadProfiles()
        onDispose { presenter.detachView() }
    }

    matchesView(
        profiles = profiles,
        onBack = onBack,
    )
}

private suspend fun filterOnlyMatches(
    allProfiles: List<UserProfile>,
    swipeRepository: com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository,
    currentUserId: com.pamn.ggmatch.app.architecture.model.user.UserId
): List<UserProfile> {
    val myHistory = (swipeRepository.get(currentUserId) as? com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok)
        ?.value ?: return emptyList()

    return allProfiles.filter { candidate ->
        val iLikedCandidate = myHistory.items[candidate.id]?.type == com.pamn.ggmatch.app.architecture.model.swipe.SwipeType.LIKE

        if (iLikedCandidate) {
            val candidateHistory = (swipeRepository.get(candidate.id) as? com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok)
                ?.value
            val candidateLikedMe = candidateHistory?.items?.get(currentUserId)?.type == com.pamn.ggmatch.app.architecture.model.swipe.SwipeType.LIKE

            candidateLikedMe
        } else {
            false
        }
    }
}