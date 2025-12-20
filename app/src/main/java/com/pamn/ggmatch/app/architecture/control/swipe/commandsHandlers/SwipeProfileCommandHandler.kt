package com.pamn.ggmatch.app.architecture.control.swipe.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.swipe.commands.SwipeProfileCommand
import com.pamn.ggmatch.app.architecture.io.swipe.SwipeHistoryRepository
import com.pamn.ggmatch.app.architecture.model.swipe.Swipe
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeHistory
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeType
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class SwipeProfileCommandHandler(
    private val repository: SwipeHistoryRepository,
    private val clock: Clock = Clock.System,
) : CommandHandler<SwipeProfileCommand, Boolean> {
    override suspend fun invoke(command: SwipeProfileCommand): Result<Boolean, AppError> {
        val now: Instant = clock.now()

        val myHistory =
            when (val getResult = repository.get(command.fromUserId)) {
                is Result.Ok -> getResult.value ?: SwipeHistory.create(command.fromUserId, now)
                is Result.Error -> return Result.Error(getResult.error)
            }

        var isMatch = false
        if (command.decision == SwipeType.LIKE) {
            val counterpartHistory = repository.get(command.toUserId)
            if (counterpartHistory is Result.Ok) {
                val swipeFromOther = counterpartHistory.value?.items?.get(command.fromUserId)
                if (swipeFromOther?.type == SwipeType.LIKE) {
                    isMatch = true
                }
            }
        }

        val newSwipe =
            Swipe(
                fromUserId = command.fromUserId,
                toUserId = command.toUserId,
                type = command.decision,
                createdAt = now,
                updatedAt = now,
            )

        val updatedProfile = myHistory.add(newSwipe)

        return when (val saveResult = repository.addOrUpdate(updatedProfile)) {
            is Result.Ok -> Result.Ok(isMatch)
            is Result.Error -> Result.Error(saveResult.error)
        }
    }
}
