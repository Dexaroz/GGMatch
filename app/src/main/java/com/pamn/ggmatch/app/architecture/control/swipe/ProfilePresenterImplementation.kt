package com.pamn.ggmatch.app.architecture.control.swipe

import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.control.swipe.commands.PreviousProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.ProfileNavigator
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProfilePresenterImplementation(
    private val view: ProfileView?,
    private val navigator: ProfileNavigator,
    private val nextProfileCommandHandler: CommandHandler<NextProfileCommand, Unit>,
    private val previousProfileCommandHandler: CommandHandler<PreviousProfileCommand, Unit>,
    private val scope: CoroutineScope,
    private val currentUserId: UserId,
) : ProfilePresenter {
    private val nextCommand = NextProfileCommand(currentUserId)
    private val previousCommand = PreviousProfileCommand(currentUserId)

    private var currentProfile: UserProfile = navigator.current()

    override fun init() {
        view?.showProfile(currentProfile)
    }

    override fun onNextClicked() {
        scope.launch {
            when (val result = nextProfileCommandHandler(nextCommand)) {
                is Result.Ok -> {
                }
                is Result.Error -> {
                }
            }
        }
    }

    override fun onPreviousClicked() {
        scope.launch {
            when (val result = previousProfileCommandHandler(previousCommand)) {
                is Result.Ok -> {
                }
                is Result.Error -> {
                }
            }
        }
    }

    fun navigator(): ProfileNavigator = navigator

    fun show(profile: UserProfile) {
        currentProfile = profile
        view?.showProfile(currentProfile)
    }
}