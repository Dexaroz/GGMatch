package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.swipe.ProfilePresenterProvider
import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class NextProfileCommandHandler(
    private val presenterProvider: ProfilePresenterProvider,
) : CommandHandler<NextProfileCommand, Unit> {
    private val presenter by lazy { presenterProvider.get() }

    override suspend operator fun invoke(command: NextProfileCommand): Result<Unit, AppError> {
        val newProfile: UserProfile? = presenter.navigator().next()

        if (newProfile != null) {
            presenter.show(newProfile)
            return Result.Ok(Unit)
        } else {
            return Result.Ok(Unit)
        }
    }
}
