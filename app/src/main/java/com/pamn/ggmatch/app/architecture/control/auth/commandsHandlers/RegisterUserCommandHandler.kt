package com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers

import android.util.Log
import com.pamn.ggmatch.app.architecture.control.auth.commands.RegisterUserCommand
import com.pamn.ggmatch.app.architecture.control.profile.commands.EnsureUserProfileExistsCommand
import com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers.EnsureUserProfileExistsCommandHandler
import com.pamn.ggmatch.app.architecture.io.user.AuthRepository
import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.model.user.User
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class RegisterUserCommandHandler(
    private val authRepository: AuthRepository,
    private val ensureUserProfileExists: EnsureUserProfileExistsCommandHandler,
) : CommandHandler<RegisterUserCommand, User> {

    override suspend operator fun invoke(command: RegisterUserCommand): Result<User, AppError> {
        val email = Email(command.email)

        return when (
            val reg =
                authRepository.register(
                    email = email,
                    password = command.password,
                )
        ) {
            is Result.Error -> reg
            is Result.Ok -> {
                val ensured = ensureUserProfileExists(EnsureUserProfileExistsCommand(reg.value.id))
                if (ensured is Result.Error) {
                    println("WARN: registered user but profile creation failed: ${ensured.error}")
                }
                reg
            }
        }
    }
}
