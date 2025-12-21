package com.pamn.ggmatch.app.architecture.model.chats

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject
import kotlinx.datetime.Instant

data class ConversationSummary(
    val id: ConversationId,
    val otherUserId: UserId,
    val lastMessageText: String?,
    val lastMessageSenderId: UserId?,
    val lastMessageAt: Instant?,
    val updatedAt: Instant,
) : ValueObject
