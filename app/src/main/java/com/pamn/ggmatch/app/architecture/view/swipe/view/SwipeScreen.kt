package com.pamn.ggmatch.app.architecture.view.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamn.ggmatch.app.AppContainer
import com.pamn.ggmatch.app.architecture.control.matching.navigator.ProfileNavigator
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.NextProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.SwipeProfileCommandHandler
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.view.swipe.SwipeTextVariables.EMPTY_DECK_MESSAGE
import com.pamn.ggmatch.app.architecture.view.swipe.SwipeTextVariables.EMPTY_DECK_SUGGESTION
import com.pamn.ggmatch.app.architecture.view.swipe.view.swipeView

class ComposeProfileViewImplementation(
    initialProfile: UserProfile?,
) : ProfileView {
    val currentProfileState: MutableState<UserProfile?> = mutableStateOf(initialProfile)
    val errorState: MutableState<String?> = mutableStateOf(null)

    val isDeckEmptyState: MutableState<Boolean> = mutableStateOf(initialProfile == null)

    override fun showProfile(profile: UserProfile?) {
        if (profile == null) {
            currentProfileState.value = null
            isDeckEmptyState.value = true
        } else {
            currentProfileState.value = profile
            isDeckEmptyState.value = false
        }
        errorState.value = null
    }

    override fun showError(message: String) {
        currentProfileState.value = null
        isDeckEmptyState.value = false
        errorState.value = message
    }
}

@Composable
fun swipeScreen(
    navigator: ProfileNavigator,
    swipeInteractionsRepository: SwipeHistoryRepository = AppContainer.swipeInteractionsRepository,
) {
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        val result = navigator.load()
        when (result) {
            is com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Ok -> {
                isLoading = false
            }
            is com.pamn.ggmatch.app.architecture.sharedKernel.result.Result.Error -> {
                loadError = "No se pudieron cargar los perfiles"
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.CircularProgressIndicator(color = Color.White)
        }
        return
    }

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
            SwipeProfileCommandHandler(repository = swipeInteractionsRepository)
        }

    val presenter =
        remember {
            ProfilePresenterImplementation(
                view = view,
                nextProfileCommandHandler = nextProfileCommandHandler,
                swipeProfileCommandHandler = swipeProfileCommandHandler,
                scope = scope,
                currentProfile = initialProfile,
                currentUserId = AppContainer.currentUserId,
            ).also { it.init() }
        }

    when {
        loadError != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(loadError!!, color = Color.Red)
            }
        }
        view.isDeckEmptyState.value -> {
            emptyProfilesView()
        }
        view.currentProfileState.value != null -> {
            val currentProfile = view.currentProfileState.value!!
            swipeView(
                currentCard = currentProfile,
                onLike = { presenter.onLikeClicked(currentProfile) },
                onDislike = { presenter.onDislikeClicked(currentProfile) },
                errorMessage = view.errorState.value,
            )
        }
    }
}

@Composable
fun emptyProfilesView() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFF212121)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "⚠️ $EMPTY_DECK_MESSAGE",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                text = EMPTY_DECK_SUGGESTION,
                color = Color.LightGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}
