package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.matching.navigator.ProfileNavigator
import com.pamn.ggmatch.app.architecture.control.swipe.commands.NextProfileCommand
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class NextProfileCommandHandler(
    private val navigator: ProfileNavigator,
) : CommandHandler<NextProfileCommand, UserProfile?> {
    override suspend fun invoke(command: NextProfileCommand): Result<UserProfile?, AppError> {
        return try {
            val nextProfile = navigator.next() // puede ser null
            Result.Ok(nextProfile)
        } catch (e: Exception) {
            Result.Error(
                AppError.Unexpected(
                    message = e.message ?: "Error al cargar siguiente perfil",
                    cause = e,
                ),
            )
        }
    }
}
