package com.pamn.ggmatch.app.architecture.control.auth.commands

import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class LoginWithGoogleCommand(
    val idToken: String,
) : Command
