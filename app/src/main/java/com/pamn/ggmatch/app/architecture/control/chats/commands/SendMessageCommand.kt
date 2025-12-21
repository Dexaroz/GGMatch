package com.pamn.ggmatch.app.architecture.control.chats.commands

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class SendMessageCommand(
    val conversationIdRaw: String,
    val from: UserId,
    val to: UserId,
    val text: String,
) : Command
