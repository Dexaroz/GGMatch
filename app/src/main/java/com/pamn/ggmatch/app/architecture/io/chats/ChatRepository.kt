package com.pamn.ggmatch.app.architecture.io.chats

import com.pamn.ggmatch.app.architecture.model.chats.ChatMessage
import com.pamn.ggmatch.app.architecture.model.chats.ConversationId
import com.pamn.ggmatch.app.architecture.model.chats.ConversationSummary
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun ensureConversationForMatch(
        me: UserId,
        other: UserId,
    ): Result<ConversationId, AppError>

    fun observeConversations(
        me: UserId,
    ): Flow<Result<List<ConversationSummary>, AppError>>

    fun observeMessages(
        conversationId: ConversationId,
    ): Flow<Result<List<ChatMessage>, AppError>>

    suspend fun sendMessage(
        conversationId: ConversationId,
        from: UserId,
        to: UserId,
        text: String,
        sentAtEpochMs: Long,
    ): Result<Unit, AppError>
}