package com.pamn.ggmatch.app.architecture.model.chats

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject
import kotlinx.datetime.Instant

data class ChatMessage(
    val id: MessageId,
    val conversationId: ConversationId,
    val senderId: UserId,
    val text: MessageText,
    val sentAt: Instant,
) : ValueObject
