package com.pamn.ggmatch.app.architecture.model.swipe

import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

data class Swipe(
    val fromUserId: UserId,
    val toUserId: UserId,
    val type: SwipeType,
    val createdAt: Instant,
    val updatedAt: Instant,
)
