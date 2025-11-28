package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler

data class AuthCommandHandlers(
    val registerUser: RegisterUserCommandHandler,
    val loginUser: LoginUserCommandHandler,
)
