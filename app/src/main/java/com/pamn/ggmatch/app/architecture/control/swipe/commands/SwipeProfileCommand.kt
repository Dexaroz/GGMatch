package com.pamn.ggmatch.app.architecture.control.swipe.commands

import com.pamn.ggmatch.app.architecture.model.swipe.SwipeDecision
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.control.Command

data class SwipeProfileCommand(
    val fromUserId: UserId,
    val toUserId: UserId,
    val decision: SwipeDecision,
) : Command
