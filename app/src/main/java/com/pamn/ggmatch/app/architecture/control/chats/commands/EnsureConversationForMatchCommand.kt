package com.pamn.ggmatch.app.architecture.control.chats.commands

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class EnsureConversationForMatchCommand(
    val me: UserId,
    val other: UserId,
) : Command

