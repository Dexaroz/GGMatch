package com.pamn.ggmatch.app.architecture.model.swipe

import com.pamn.ggmatch.app.architecture.model.user.UserId
import kotlinx.datetime.Instant

data class SwipeInteractionsProfile(
    val userId: UserId,
    val interactions: Map<UserId, SwipeInteraction>,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun create(
            userId: UserId,
            createdAt: Instant,
        ): SwipeInteractionsProfile {
            return SwipeInteractionsProfile(
                userId = userId,
                interactions = emptyMap(),
                createdAt = createdAt,
                updatedAt = createdAt,
            )
        }

        fun fromPersistence(
            userId: UserId,
            interactions: Map<UserId, SwipeInteraction>,
            createdAt: Instant,
            updatedAt: Instant,
        ): SwipeInteractionsProfile {
            return SwipeInteractionsProfile(
                userId = userId,
                interactions = interactions,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }

    fun addInteraction(newInteraction: SwipeInteraction): SwipeInteractionsProfile {
        require(newInteraction.fromUserId == this.userId) {
            "La interacción debe ser del usuario propietario del perfil."
        }

        val updatedInteractions = this.interactions.toMutableMap()

        updatedInteractions[newInteraction.toUserId] = newInteraction

        return this.copy(
            interactions = updatedInteractions,
            updatedAt = newInteraction.updatedAt,
        )
    }
}
