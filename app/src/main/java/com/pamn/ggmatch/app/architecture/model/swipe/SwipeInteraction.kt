package com.pamn.ggmatch.app.architecture.model.swipe

import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

data class SwipeInteraction(
    val fromUserId: UserId,
    val toUserId: UserId,
    val decision: SwipeDecision,
    val createdAt: Instant,
    val updatedAt: Instant,
)
