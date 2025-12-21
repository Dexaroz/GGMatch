package com.pamn.ggmatch.app.architecture.control.chats.commandHandlers

import com.pamn.ggmatch.app.architecture.control.chats.commands.EnsureConversationForMatchCommand
import com.pamn.ggmatch.app.architecture.io.chats.ChatRepository
import com.pamn.ggmatch.app.architecture.model.chats.ConversationId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

class EnsureConversationForMatchCommandHandler(
    private val chatRepository: ChatRepository,
) : CommandHandler<EnsureConversationForMatchCommand, ConversationId> {
    override suspend fun invoke(command: EnsureConversationForMatchCommand): Result<ConversationId, AppError> {
        return chatRepository.ensureConversationForMatch(command.me, command.other)
    }
}
