package com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.auth.commands.SendPasswordResetEmailCommand
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class SendPasswordResetEmailCommandHandler(
    private val authRepository: AuthRepository,
) : CommandHandler<SendPasswordResetEmailCommand, Unit> {

    override suspend operator fun invoke(command: SendPasswordResetEmailCommand): Result<Unit, AppError> {
        return authRepository.sendPasswordResetEmail(
            email = command.email,
        )
    }
}