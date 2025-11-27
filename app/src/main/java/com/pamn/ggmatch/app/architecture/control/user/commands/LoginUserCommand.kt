package com.pamn.ggmatch.app.architecture.control.user.commands

import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class LoginUserCommand(
    val email: String,
    val password: String,
) : Command
