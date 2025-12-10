package com.pamn.ggmatch.app.architecture.view.swipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterProvider
import com.pamn.ggmatch.app.architecture.control.swipe.ProfileView
import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commands.PreviousProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.NextProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers.PreviousProfileCommandHandler
import com.pamn.ggmatch.app.architecture.control.swipe.view.swipeView
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler

class ComposeProfileViewImplementation(
    initialProfile: UserProfile,
) : ProfileView {
    val currentProfileState: MutableState<UserProfile> = mutableStateOf(initialProfile)

    override fun showProfile(profile: UserProfile) {
        currentProfileState.value = profile
    }
}

@Composable
fun swipeScreen(
    navigator: ProfileNavigator,
    currentUserId: UserId,
) {
    val scope = rememberCoroutineScope()
    val initialProfile = navigator.current()
    val viewImplementation = remember { ComposeProfileViewImplementation(initialProfile) }

    val presenter = remember {
        lateinit var pInstance: ProfilePresenterImplementation

        val presenterProvider = object : ProfilePresenterProvider {
            override fun get(): ProfilePresenterImplementation = pInstance
        }

        val nextHandler = NextProfileCommandHandler(presenterProvider = presenterProvider)
        val prevHandler = PreviousProfileCommandHandler(presenterProvider = presenterProvider)

        pInstance = ProfilePresenterImplementation(
            view = viewImplementation,
            navigator = navigator,
            nextProfileCommandHandler = nextHandler as CommandHandler<NextProfileCommand, Unit>,
            previousProfileCommandHandler = prevHandler as CommandHandler<PreviousProfileCommand, Unit>,
            scope = scope,
            currentUserId = currentUserId,
        )

        pInstance
    }

    val currentCard = viewImplementation.currentProfileState.value

    swipeView(
        currentCard = currentCard,
        onNext = presenter::onNextClicked,
        onPrevious = presenter::onPreviousClicked,
    )
}