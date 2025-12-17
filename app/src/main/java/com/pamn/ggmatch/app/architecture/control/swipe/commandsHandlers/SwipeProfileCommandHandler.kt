package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.swipe.commands.SwipeProfileCommand
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeInteractionsRepository
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeInteraction
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeInteractionsProfile
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SwipeProfileCommandHandler(
    private val repository: SwipeInteractionsRepository,
    private val clock: Clock = Clock.System,
) : CommandHandler<SwipeProfileCommand, Unit> {
    override suspend fun invoke(command: SwipeProfileCommand): Result<Unit, AppError> {
        val now: Instant = clock.now()

        val interactionsProfile =
            when (val getResult = repository.get(command.fromUserId)) {
                is Result.Ok ->
                    getResult.value ?: SwipeInteractionsProfile.create(
                        userId = command.fromUserId,
                        createdAt = now,
                    )
                is Result.Error -> return Result.Error(getResult.error)
            }

        val newInteraction =
            SwipeInteraction(
                fromUserId = command.fromUserId,
                toUserId = command.toUserId,
                decision = command.decision,
                createdAt = now,
                updatedAt = now,
            )

        val updatedProfile = interactionsProfile.addInteraction(newInteraction)

        return when (val saveResult = repository.addOrUpdate(updatedProfile)) {
            is Result.Ok -> Result.Ok(Unit)
            is Result.Error -> Result.Error(saveResult.error)
        }
    }
}
