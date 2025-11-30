package com.pamn.ggmatch.app.architecture.control.auth.commands

import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class RegisterUserCommand(
    val email: String,
    val password: String,
) : Command
