package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commands.SwipeProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeType
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
class ProfilePresenterImplementation(
    private val view: ProfileView,
    private val swipeProfileCommandHandler: CommandHandler<SwipeProfileCommand, Boolean>,
    private val nextProfileCommandHandler: CommandHandler<NextProfileCommand, UserProfile?>,
    private val scope: CoroutineScope,
    currentProfile: UserProfile?,
    private val currentUserId: UserId,
) : ProfilePresenter {
    private var profile: UserProfile? = currentProfile
    var onMatchFound: ((UserProfile) -> Unit)? = null

    override fun init() {
        profile?.let { view.showProfile(it) } ?: onNextClicked()
    }

    override fun onNextClicked() {
        scope.launch {
            loadNextProfile()
        }
    }

    override fun onLikeClicked(targetProfile: UserProfile) {
        handleSwipe(targetProfile, SwipeType.LIKE)
    }

    override fun onDislikeClicked(targetProfile: UserProfile) {
        handleSwipe(targetProfile, SwipeType.DISLIKE)
    }

    private fun handleSwipe(
        targetProfile: UserProfile,
        decision: SwipeType,
    ) {
        val swipeCommand = SwipeProfileCommand(
            fromUserId = currentUserId,
            toUserId = targetProfile.id,
            decision = decision,
        )

        scope.launch {
            when (val swipeResult = swipeProfileCommandHandler.invoke(swipeCommand)) {
                is Result.Ok -> {
                    // Si el comando devuelve true, disparamos el evento de Match
                    val isMatch = swipeResult.value
                    if (isMatch && decision == SwipeType.LIKE) {
                        onMatchFound?.invoke(targetProfile)
                    }
                    loadNextProfile()
                }
                is Result.Error -> view.showError("Error al guardar la interacción.")
            }
        }
    }

    private suspend fun loadNextProfile() {
        when (val result = nextProfileCommandHandler.invoke(NextProfileCommand(currentUserId))) {
            is Result.Ok -> {
                profile = result.value
                view.showProfile(profile)
            }
            is Result.Error -> view.showError("No se pudo cargar el siguiente perfil.")
        }
    }
}