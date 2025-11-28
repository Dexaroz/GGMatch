package com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.auth.commands.RegisterUserCommand
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.model.user.Username
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class RegisterUserCommandHandler(
    private val authRepository: AuthRepository,
) : CommandHandler<RegisterUserCommand, User> {
    override suspend operator fun invoke(command: RegisterUserCommand): Result<User, AppError> {
        val email = Email(command.email)
        val username = Username(command.username)

        return authRepository.register(
            email = email,
            password = command.password,
            username = username,
        )
    }
}
