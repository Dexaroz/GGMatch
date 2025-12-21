package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginWithGoogleCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.chats.commandHandlers.EnsureConversationForMatchCommandHandler
import com.pamn.ggmatch.app.architecture.control.chats.commandHandlers.SendMessageCommandHandler
import com.pamn.ggmatch.app.architecture.io.chats.ChatRepository

data class ChatController(
    private val chatRepository: ChatRepository,
    private val ensureConversation: EnsureConversationForMatchCommandHandler,
    private val sendMessage: SendMessageCommandHandler,
)
