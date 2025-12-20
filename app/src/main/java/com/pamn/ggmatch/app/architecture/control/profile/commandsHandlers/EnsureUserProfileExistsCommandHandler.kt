package com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.profile.commands.EnsureUserProfileExistsCommand
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class EnsureUserProfileExistsCommandHandler(
    private val profileRepository: ProfileRepository,
    private val timeProvider: TimeProvider,
) : CommandHandler<EnsureUserProfileExistsCommand, Unit> {

    override suspend operator fun invoke(
        command: EnsureUserProfileExistsCommand,
    ): Result<Unit, AppError> {
        println("🔥 EnsureUserProfileExists CALLED for userId=${command.userId.value}")

        return when (val existing = profileRepository.get(command.userId)) {
            is Result.Error -> {
                println("❌ Error getting profile: ${existing.error}")
                existing
            }
            is Result.Ok -> {
                if (existing.value != null) {
                    println("ℹ️ Profile already exists")
                    return Result.Ok(Unit)
                }

                println("🟢 Creating new profile in Firestore")
                val profile =
                    UserProfile.createNew(
                        id = command.userId,
                        timeProvider = timeProvider,
                    )

                profileRepository.add(profile)
            }
        }
    }
}