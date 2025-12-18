package com.pamn.ggmatch.app.architecture.control.auth.commands

import com.pamn.ggmatch.app.architecture.model.user.Email
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class SendPasswordResetEmailCommand(
    val email: Email,
) : Command
