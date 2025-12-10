package com.pamn.ggmatch.app.architecture.control.swipe.commands

import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class PreviousProfileCommand(
    val requestingUserId: UserId,
) : Command
