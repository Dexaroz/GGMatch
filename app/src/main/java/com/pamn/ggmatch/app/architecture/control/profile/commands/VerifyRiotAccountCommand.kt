package com.pamn.ggmatch.app.architecture.control.profile.commands

import com.pamn.ggmatch.app.architecture.model.user.UserId

data class VerifyRiotAccountCommand(
    val userId: UserId,
    val gameName: String,
    val tagLine: String,
)
