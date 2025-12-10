package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterImplementation
import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterProvider
import com.pamn.ggmatch.app.architecture.control.swipe.commands.PreviousProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class PreviousProfileCommandHandler(
    private val presenterProvider: ProfilePresenterProvider,
) : CommandHandler<PreviousProfileCommand, Unit> {

    private val presenter by lazy { presenterProvider.get() }

    override suspend operator fun invoke(command: PreviousProfileCommand): Result<Unit, AppError> {
        val newProfile: UserProfile? = presenter.navigator().previous()

        if (newProfile != null) {
            presenter.show(newProfile)
            return Result.Ok(Unit)
        } else {
            return Result.Ok(Unit)
        }
    }
}