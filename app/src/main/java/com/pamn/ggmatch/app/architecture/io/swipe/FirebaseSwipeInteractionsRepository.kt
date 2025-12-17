package com.pamn.ggmatch.app.architecture.io.swipe

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pamn.ggmatch.app.architecture.io.FirebaseRepository
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeDecision
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeInteraction
import com.pamn.ggmatch.app.architecture.model.swipe.SwipeInteractionsProfile
import com.pamn.ggmatch.app.architecture.model.user.UserId
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import kotlinx.datetime.Instant

class FirebaseSwipeInteractionsRepository(
    firestore: FirebaseFirestore,
) : FirebaseRepository<UserId, SwipeInteractionsProfile>(
        firestore = firestore,
        collectionName = "swipe-interactions",
    ),
    SwipeInteractionsRepository {
    override fun getId(entity: SwipeInteractionsProfile): UserId = entity.userId

    override fun idToString(id: UserId): String = id.value

    override fun stringToId(id: String): UserId = UserId(id)

    override fun toDocument(entity: SwipeInteractionsProfile): Map<String, Any?> {
        return mapOf(
            "interactions" to
                entity.interactions.mapKeys { (targetId, _) ->
                    idToString(targetId)
                }.mapValues { (_, interaction) ->
                    mapOf(
                        "decision" to interaction.decision.name,
                        "createdAtEpochMs" to interaction.createdAt.toEpochMilliseconds(),
                        "updatedAtEpochMs" to interaction.updatedAt.toEpochMilliseconds(),
                    )
                },
            "createdAtEpochMs" to entity.createdAt.toEpochMilliseconds(),
            "updatedAtEpochMs" to entity.updatedAt.toEpochMilliseconds(),
        )
    }

    override fun fromDocument(
        id: UserId,
        doc: DocumentSnapshot,
    ): SwipeInteractionsProfile {
        val rawInteractions =
            doc.get("interactions") as? Map<*, *> ?: emptyMap<String, Any?>()

        val interactions =
            rawInteractions.mapNotNull { (key, value) ->
                val targetId = (key as? String)?.let(::UserId) ?: return@mapNotNull null
                val data = value as? Map<*, *> ?: return@mapNotNull null

                val decision =
                    (data["decision"] as? String)
                        ?.let { runCatching { SwipeDecision.valueOf(it) }.getOrNull() }
                        ?: return@mapNotNull null

                val createdAt =
                    (data["createdAtEpochMs"] as? Long)?.let(Instant::fromEpochMilliseconds)
                        ?: return@mapNotNull null

                val updatedAt =
                    (data["updatedAtEpochMs"] as? Long)?.let(Instant::fromEpochMilliseconds)
                        ?: createdAt

                targetId to
                    SwipeInteraction(
                        fromUserId = id,
                        toUserId = targetId,
                        decision = decision,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                    )
            }.toMap()

        val createdAt =
            doc.getLong("createdAtEpochMs")
                ?.let(Instant::fromEpochMilliseconds)
                ?: error("Missing createdAtEpochMs for user ${id.value}")

        val updatedAt =
            doc.getLong("updatedAtEpochMs")
                ?.let(Instant::fromEpochMilliseconds)
                ?: createdAt

        return SwipeInteractionsProfile.fromPersistence(
            userId = id,
            interactions = interactions,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    override suspend fun addOrUpdate(profile: SwipeInteractionsProfile): Result<Unit, AppError> =
        when (val existing = get(profile.userId)) {
            is Result.Error -> existing
            is Result.Ok ->
                if (existing.value == null) {
                    add(profile)
                } else {
                    update(profile)
                }
        }
}
