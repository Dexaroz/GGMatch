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
    private val swipeProfileCommandHandler: CommandHandler<SwipeProfileCommand, Unit>,
    private val nextProfileCommandHandler: CommandHandler<NextProfileCommand, UserProfile?>,
    private val scope: CoroutineScope,
    currentProfile: UserProfile?,
    private val currentUserId: UserId,
) : ProfilePresenter {
    private var profile: UserProfile? = currentProfile

    override fun init() {
        profile?.let { view.showProfile(it) } ?: run {
            scope.launch {
                when (val result = nextProfileCommandHandler.invoke(NextProfileCommand(currentUserId))) {
                    is Result.Ok -> view.showProfile(result.value)
                    is Result.Error -> view.showError(result.error.message)
                }
            }
        }
    }

    override fun onNextClicked() {
        scope.launch {
            when (val result = nextProfileCommandHandler.invoke(NextProfileCommand(currentUserId))) {
                is Result.Ok -> view.showProfile(result.value)
                is Result.Error -> view.showError(result.error.message)
            }
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
        val swipeCommand =
            SwipeProfileCommand(
                fromUserId = currentUserId,
                toUserId = targetProfile.id,
                decision = decision,
            )

        scope.launch {
            when (swipeProfileCommandHandler.invoke(swipeCommand)) {
                is Result.Ok -> {
                    when (val result = nextProfileCommandHandler.invoke(NextProfileCommand(currentUserId))) {
                        is Result.Ok -> view.showProfile(result.value)
                        is Result.Error -> view.showError("Interacción guardada, pero no se pudo cargar el siguiente perfil.")
                    }
                }
                is Result.Error -> view.showError("Error al guardar la interacción.")
            }
        }
    }
}
