package com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.auth.commands.LoginUserCommand
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class LoginUserCommandHandler(
    private val authRepository: AuthRepository,
) : CommandHandler<LoginUserCommand, User> {
    override suspend operator fun invoke(command: LoginUserCommand): Result<User, AppError> {
        val email = Email(command.email)

        return authRepository.login(
            email = email,
            password = command.password,
        )
    }
}
