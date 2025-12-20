package com.pamn.ggmatch.app.architecture.control.profile.commands

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class EnsureUserProfileExistsCommand(
    val userId: UserId,
) : Command
