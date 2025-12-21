package com.pamn.ggmatch.app.architecture.control.chats.commandHandlers

import com.pamn.ggmatch.app.architecture.control.chats.commands.SendMessageCommand
import com.pamn.ggmatch.app.architecture.io.chats.ChatRepository
import com.pamn.ggmatch.app.architecture.model.chats.ConversationId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class SendMessageCommandHandler(
    private val chatRepository: ChatRepository,
    private val timeProvider: TimeProvider,
) : CommandHandler<SendMessageCommand, Unit> {

    override suspend fun invoke(command: SendMessageCommand): Result<Unit, AppError> {
        val convId = ConversationId(command.conversationIdRaw)
        val now = timeProvider.now().toEpochMilliseconds()

        return chatRepository.sendMessage(
            conversationId = convId,
            from = command.from,
            to = command.to,
            text = command.text,
            sentAtEpochMs = now,
        )
    }
}