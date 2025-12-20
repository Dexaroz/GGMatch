package com.pamn.ggmatch.app.architecture.model.swipe

import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

data class SwipeHistory(
    val userId: UserId,
    val items: Map<UserId, Swipe>,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun create(
            userId: UserId,
            createdAt: Instant,
        ): SwipeHistory {
            return SwipeHistory(
                userId = userId,
                items = emptyMap(),
                createdAt = createdAt,
                updatedAt = createdAt,
            )
        }

        fun fromPersistence(
            userId: UserId,
            items: Map<UserId, Swipe>,
            createdAt: Instant,
            updatedAt: Instant,
        ): SwipeHistory {
            return SwipeHistory(
                userId = userId,
                items = items,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }

    fun add(newSwipe: Swipe): SwipeHistory {
        require(newSwipe.fromUserId == this.userId) {
            "The swipe must belong to the history owner."
        }

        val updatedItems = this.items.toMutableMap()
        updatedItems[newSwipe.toUserId] = newSwipe

        return this.copy(
            items = updatedItems,
            updatedAt = newSwipe.updatedAt,
        )
    }
}
