package com.pamn.ggmatch.app.controllers

import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginUserCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.LoginWithGoogleCommandHandler
import com.pamn.ggmatch.app.architecture.control.auth.commandsHandlers.RegisterUserCommandHandler

data class AuthController(
    val registerUser: RegisterUserCommandHandler,
    val loginUser: LoginUserCommandHandler,
    val loginWithGoogle: LoginWithGoogleCommandHandler,
)
