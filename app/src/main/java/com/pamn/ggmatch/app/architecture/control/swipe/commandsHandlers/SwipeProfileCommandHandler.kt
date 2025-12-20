package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.swipe.commands.SwipeProfileCommand
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.swipe.Swipe
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeHistory
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SwipeProfileCommandHandler(
    private val repository: SwipeHistoryRepository,
    private val clock: Clock = Clock.System,
) : CommandHandler<SwipeProfileCommand, Unit> {
    override suspend fun invoke(command: SwipeProfileCommand): Result<Unit, AppError> {
        val now: Instant = clock.now()

        val interactionsProfile =
            when (val getResult = repository.get(command.fromUserId)) {
                is Result.Ok ->
                    getResult.value ?: SwipeHistory.create(
                        userId = command.fromUserId,
                        createdAt = now,
                    )
                is Result.Error -> return Result.Error(getResult.error)
            }

        val newInteraction =
            Swipe(
                fromUserId = command.fromUserId,
                toUserId = command.toUserId,
                type = command.decision,
                createdAt = now,
                updatedAt = now,
            )

        val updatedProfile = interactionsProfile.add(newInteraction)

        return when (val saveResult = repository.addOrUpdate(updatedProfile)) {
            is Result.Ok -> Result.Ok(Unit)
            is Result.Error -> Result.Error(saveResult.error)
        }
    }
}
