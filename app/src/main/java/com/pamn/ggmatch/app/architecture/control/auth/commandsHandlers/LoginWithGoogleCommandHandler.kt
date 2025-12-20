package com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginWithGoogleCommand
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class LoginWithGoogleCommandHandler(
    private val authRepository: AuthRepository,
) : CommandHandler<LoginWithGoogleCommand, User> {
    override suspend operator fun invoke(command: LoginWithGoogleCommand): Result<User, AppError> =
        authRepository.loginWithGoogle(command.idToken)
}
